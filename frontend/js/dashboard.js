// Dashboard logic for fetching roles and starting interviews

document.addEventListener('DOMContentLoaded', async () => {
  requireAuth();
  const { userId, username } = getCurrentUser();

  const welcomeEl = document.getElementById('welcomeUser');
  if (welcomeEl && username) {
    welcomeEl.textContent = `Welcome back, ${username}`;
  }

  const roleGrid = document.getElementById('roleGrid');
  if (roleGrid) {
    try {
      roleGrid.innerHTML = '<p style="color: var(--text-muted);">Loading available roles...</p>';
      const response = await fetch(`${BASE_URL}/api/roles`);
      if (!response.ok) {
        throw new Error('Failed to fetch roles');
      }
      const roles = await response.json();

      if (!roles || roles.length === 0) {
        roleGrid.innerHTML = '<p style="color: var(--text-muted);">No interview roles available at this time.</p>';
        return;
      }

      roleGrid.innerHTML = '';
      roles.forEach(role => {
        const card = document.createElement('div');
        card.className = 'role-card';
        card.innerHTML = `
          <div>
            <h3 class="role-title">${escapeHtml(role.name)}</h3>
            <p class="role-desc">${escapeHtml(role.description || 'Practice technical and behavioral questions tailored for this role.')}</p>
          </div>
          <button class="btn start-interview-btn" data-role-id="${role.id}">Start Interview</button>
        `;
        roleGrid.appendChild(card);
      });

      // Attach event listeners to start buttons
      document.querySelectorAll('.start-interview-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
          const roleId = e.target.getAttribute('data-role-id');
          await startInterviewSession(userId, roleId, e.target);
        });
      });

    } catch (err) {
      console.error(err);
      roleGrid.innerHTML = '<p style="color: var(--error-text);">Failed to load roles. Please check backend connection.</p>';
      showToast('Error loading roles', 'error');
    }
  }
});

async function startInterviewSession(userId, roleId, btnElement) {
  btnElement.disabled = true;
  btnElement.innerHTML = '<span class="spinner"></span> Starting...';

  try {
    const response = await fetch(`${BASE_URL}/api/interview/start?userId=${userId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ roleId: parseInt(roleId, 10) })
    });

    const data = await response.json();
    if (response.ok && data.sessionId) {
      localStorage.setItem('currentSessionId', data.sessionId);
      if (data.firstQuestion) {
        sessionStorage.setItem('firstQuestion', JSON.stringify(data.firstQuestion));
      }
      window.location.href = `interview.html?sessionId=${data.sessionId}`;
    } else {
      showToast(data.message || 'Failed to start interview session', 'error');
      btnElement.disabled = false;
      btnElement.textContent = 'Start Interview';
    }
  } catch (err) {
    console.error(err);
    showToast('Network error starting interview', 'error');
    btnElement.disabled = false;
    btnElement.textContent = 'Start Interview';
  }
}

function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}
