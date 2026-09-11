// Interview result logic for rendering score, overall remark, and question accordion

document.addEventListener('DOMContentLoaded', async () => {
  requireAuth();

  const urlParams = new URLSearchParams(window.location.search);
  const sessionId = urlParams.get('sessionId');

  if (!sessionId) {
    showToast('No session ID provided', 'error');
    setTimeout(() => window.location.href = 'dashboard.html', 1500);
    return;
  }

  const container = document.getElementById('resultContainer');
  if (container) {
    try {
      container.innerHTML = '<p style="color: var(--text-muted); text-align: center; padding: 3rem;">Loading interview results...</p>';
      
      const response = await fetch(`${BASE_URL}/api/result/${sessionId}`);
      if (!response.ok) {
        throw new Error('Failed to fetch result');
      }

      const result = await response.json();
      renderResult(result);

    } catch (err) {
      console.error(err);
      container.innerHTML = '<p style="color: var(--error-text); text-align: center; padding: 3rem;">Failed to load interview results. Please try again.</p>';
      showToast('Error loading results', 'error');
    }
  }
});

function renderResult(result) {
  const container = document.getElementById('resultContainer');
  if (!container) return;

  const totalScore = result.totalScore !== undefined ? result.totalScore : 0;
  const overallRemark = result.overallRemark || 'Completed';
  const evaluatedQuestions = result.evaluatedQuestions || [];

  let html = `
    <div class="card" style="text-align: center; margin-bottom: 2rem;">
      <h1>Interview Results</h1>
      <p class="subtitle">Session #${result.sessionId} Complete</p>
      
      <div class="score-circle-container">
        <div class="score-badge-large">${totalScore}%</div>
        <h2 style="color: var(--accent-blue);">${escapeHtml(overallRemark)}</h2>
      </div>

      <div style="display: flex; justify-content: center; gap: 1rem; margin-top: 1.5rem;">
        <a href="dashboard.html" class="btn" style="width: auto;">Back to Dashboard</a>
        <a href="history.html" class="btn btn-secondary" style="width: auto;">View History</a>
      </div>
    </div>

    <h2 style="margin-bottom: 1rem;">Question Breakdown</h2>
    <div class="accordion">
  `;

  if (evaluatedQuestions.length === 0) {
    html += `<p style="color: var(--text-muted);">No evaluated questions found for this session.</p>`;
  } else {
    evaluatedQuestions.forEach((eq, index) => {
      const qNum = index + 1;
      const score = eq.similarityScore !== undefined ? eq.similarityScore : 0;
      const remark = eq.remark || 'N/A';
      const qText = eq.questionText || 'Question';
      const uAnswer = eq.userAnswer || 'No answer provided';
      const iAnswer = eq.idealAnswer || 'N/A';

      html += `
        <div class="accordion-item" id="accItem-${index}">
          <div class="accordion-header" onclick="toggleAccordion(${index})">
            <span>Q${qNum}: ${escapeHtml(qText)}</span>
            <div style="display: flex; align-items: center; gap: 1rem;">
              <span style="font-weight: 600; color: var(--accent-blue);">${score}%</span>
              <span class="question-badge">${escapeHtml(remark)}</span>
              <span>▼</span>
            </div>
          </div>
          <div class="accordion-content">
            <div style="margin-bottom: 1rem;">
              <strong>Your Answer:</strong>
              <p style="background: white; padding: 0.75rem; border-radius: 60px; border: 1px solid var(--border-color); margin-top: 0.35rem; color: var(--text-main);">${escapeHtml(uAnswer)}</p>
            </div>
            <div style="margin-bottom: 1rem;">
              <strong>Ideal Answer / Reference:</strong>
              <p style="background: white; padding: 0.75rem; border-radius: 6px; border: 1px solid var(--border-color); margin-top: 0.35rem; color: var(--text-muted);">${escapeHtml(iAnswer)}</p>
            </div>
            <div style="display: flex; gap: 2rem; font-size: 0.9rem;">
              <div><strong>Similarity Score:</strong> ${score}%</div>
              <div><strong>Remark:</strong> ${escapeHtml(remark)}</div>
            </div>
          </div>
        </div>
      `;
    });
  }

  html += `</div>`;
  container.innerHTML = html;
}

function toggleAccordion(index) {
  const item = document.getElementById(`accItem-${index}`);
  if (item) {
    item.classList.toggle('open');
  }
}

function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}
