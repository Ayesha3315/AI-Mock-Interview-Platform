// Utility functions for UI helpers, toasts, and storage

function showToast(message, type = 'success') {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.textContent = message;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transition = 'opacity 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

function getCurrentUser() {
  const userId = localStorage.getItem('userId');
  const username = localStorage.getItem('username');
  return { userId, username };
}

function requireAuth() {
  const { userId } = getCurrentUser();
  if (!userId) {
    window.location.href = 'login.html';
  }
}

function logout() {
  localStorage.removeItem('userId');
  localStorage.removeItem('username');
  localStorage.removeItem('currentSessionId');
  window.location.href = 'login.html';
}

function updateNavigation() {
  const nav = document.querySelector('nav');
  if (!nav) return;

  const { userId, username } = getCurrentUser();
  if (userId) {
    nav.innerHTML = `
      <a href="dashboard.html">Dashboard</a>
      <a href="history.html">History</a>
      <span class="user-badge">${username || 'User'}</span>
      <a href="#" id="logoutBtn" style="color: #ef4444;">Logout</a>
    `;
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
      logoutBtn.addEventListener('click', (e) => {
        e.preventDefault();
        logout();
      });
    }
  } else {
    nav.innerHTML = `
      <a href="index.html">Home</a>
      <a href="login.html">Login</a>
      <a href="register.html" class="btn" style="padding: 0.5rem 1rem; width: auto;">Register</a>
    `;
  }
}

document.addEventListener('DOMContentLoaded', () => {
  updateNavigation();
});
