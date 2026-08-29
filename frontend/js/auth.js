// Authentication logic for login and register

document.addEventListener('DOMContentLoaded', () => {
  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const username = document.getElementById('username').value.trim();
      const email = document.getElementById('email').value.trim();
      const password = document.getElementById('password').value.trim();
      const submitBtn = registerForm.querySelector('button[type="submit"]');

      if (!username || !email || !password) {
        showToast('Please fill in all fields', 'error');
        return;
      }

      submitBtn.disabled = true;
      submitBtn.innerHTML = '<span class="spinner"></span> Registering...';

      try {
        const response = await fetch(`${BASE_URL}/api/auth/register`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username, email, password })
        });

        const data = await response.json();
        if (response.ok) {
          showToast(data.message || 'Registration Successful! Please login.', 'success');
          setTimeout(() => {
            window.location.href = 'login.html';
          }, 1500);
        } else {
          showToast(data.message || 'Registration failed', 'error');
          submitBtn.disabled = false;
          submitBtn.textContent = 'Register';
        }
      } catch (err) {
        console.error(err);
        showToast('Network error connecting to backend', 'error');
        submitBtn.disabled = false;
        submitBtn.textContent = 'Register';
      }
    });
  }

  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const username = document.getElementById('username').value.trim();
      const password = document.getElementById('password').value.trim();
      const submitBtn = loginForm.querySelector('button[type="submit"]');

      if (!username || !password) {
        showToast('Please enter username and password', 'error');
        return;
      }

      submitBtn.disabled = true;
      submitBtn.innerHTML = '<span class="spinner"></span> Logging in...';

      try {
        const response = await fetch(`${BASE_URL}/api/auth/login`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username, password })
        });

        const data = await response.json();
        if (response.ok) {
          localStorage.setItem('userId', data.userId);
          localStorage.setItem('username', data.username || username);
          showToast('Login Successful!', 'success');
          setTimeout(() => {
            window.location.href = 'dashboard.html';
          }, 1000);
        } else {
          showToast(data.message || 'Invalid username or password', 'error');
          submitBtn.disabled = false;
          submitBtn.textContent = 'Login';
        }
      } catch (err) {
        console.error(err);
        showToast('Network error connecting to backend', 'error');
        submitBtn.disabled = false;
        submitBtn.textContent = 'Login';
      }
    });
  }
});
