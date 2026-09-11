// Interview history logic for fetching and rendering past interview sessions

document.addEventListener('DOMContentLoaded', async () => {
  requireAuth();
  const { userId } = getCurrentUser();

  const historyContainer = document.getElementById('historyContainer');
  if (historyContainer) {
    try {
      historyContainer.innerHTML = '<p style="color: var(--text-muted); text-align: center; padding: 3rem;">Loading interview history...</p>';

      const response = await fetch(`${BASE_URL}/api/history/${userId}`);
      if (!response.ok) {
        throw new Error('Failed to fetch history');
      }

      const historyList = await response.json();
      renderHistory(historyList);

    } catch (err) {
      console.error(err);
      historyContainer.innerHTML = '<p style="color: var(--error-text); text-align: center; padding: 3rem;">Failed to load history. Please try again.</p>';
      showToast('Error loading history', 'error');
    }
  }
});

function renderHistory(historyList) {
  const container = document.getElementById('historyContainer');
  if (!container) return;

  if (!historyList || historyList.length === 0) {
    container.innerHTML = `
      <div class="card" style="text-align: center; padding: 3rem;">
        <h3>No interview history found</h3>
        <p class="subtitle" style="margin-top: 0.5rem;">Take your first mock interview to see past results here.</p>
        <a href="dashboard.html" class="btn" style="width: auto; margin-top: 1rem;">Go to Dashboard</a>
      </div>
    `;
    return;
  }

  let html = `
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>Session ID</th>
            <th>Role</th>
            <th>Score</th>
            <th>Date</th>
            <th style="text-align: right;">Action</th>
          </tr>
        </thead>
        <tbody>
  `;

  historyList.forEach(item => {
    const sessionId = item.sessionId || item.id || 1;
    const roleName = item.roleName || 'Technical Interview';
    const score = item.totalScore !== undefined ? item.totalScore : (item.score || 0);
    const dateStr = item.completedDate ? new Date(item.completedDate).toLocaleString() : 'Recent';

    html += `
      <tr>
        <td>#${sessionId}</td>
        <td><strong>${escapeHtml(roleName)}</strong></td>
        <td><span style="font-weight: 600; color: var(--accent-blue);">${score}%</span></td>
        <td>${escapeHtml(dateStr)}</td>
        <td style="text-align: right;">
          <a href="result.html?sessionId=${sessionId}" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.85rem; width: auto;">View Result</a>
        </td>
      </tr>
    `;
  });

  html += `
        </tbody>
      </table>
    </div>
  `;

  container.innerHTML = html;
}

function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}
