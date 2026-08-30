// Interview session & question management logic

let currentSessionId = null;
let currentQuestionId = null;
let questionCount = 1;

document.addEventListener('DOMContentLoaded', async () => {
  requireAuth();
  
  const urlParams = new URLSearchParams(window.location.search);
  currentSessionId = urlParams.get('sessionId') || localStorage.getItem('currentSessionId');

  if (!currentSessionId) {
    showToast('No active interview session found', 'error');
    setTimeout(() => window.location.href = 'dashboard.html', 1500);
    return;
  }

  const storedQ = sessionStorage.getItem('firstQuestion');
  if (storedQ) {
    try {
      const q = JSON.parse(storedQ);
      currentQuestionId = q.questionId || q.id;
      const qTextEl = document.getElementById('questionText');
      if (qTextEl) {
        qTextEl.textContent = q.questionText;
      }
    } catch (e) {
      console.error(e);
    }
  }

  const answerForm = document.getElementById('answerForm');
  const userAnswerInput = document.getElementById('userAnswer');

  if (answerForm) {
    answerForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const userAnswer = userAnswerInput.value.trim();
      const submitBtn = document.getElementById('submitBtn');

      if (!userAnswer) {
        showToast('Please provide an answer before submitting', 'error');
        return;
      }

      submitBtn.disabled = true;
      submitBtn.innerHTML = '<span class="spinner"></span> Evaluating...';

      try {
        const response = await fetch(`${BASE_URL}/api/interview/answer`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            sessionId: parseInt(currentSessionId, 10),
            questionId: parseInt(currentQuestionId, 10),
            userAnswer: userAnswer
          })
        });

        const data = await response.json();
        if (response.ok) {
          // Show feedback box
          const feedbackBox = document.getElementById('feedbackBox');
          const scoreEl = document.getElementById('feedbackScore');
          const remarkEl = document.getElementById('feedbackRemark');

          scoreEl.textContent = `${data.similarityScore}%`;
          remarkEl.textContent = data.remark;
          feedbackBox.classList.add('active');

          submitBtn.textContent = 'Processing next...';

          setTimeout(() => {
            if (data.completed) {
              showToast('Interview completed! Loading results...', 'success');
              localStorage.removeItem('currentSessionId');
              setTimeout(() => {
                window.location.href = `result.html?sessionId=${currentSessionId}`;
              }, 1200);
            } else if (data.nextQuestion) {
              currentQuestionId = data.nextQuestion.questionId || data.nextQuestion.id;
              document.getElementById('questionText').textContent = data.nextQuestion.questionText;
              userAnswerInput.value = '';
              feedbackBox.classList.remove('active');
              questionCount++;
              document.getElementById('questionNumber').textContent = `Question ${questionCount}`;
              updateProgressBar(questionCount);
              submitBtn.disabled = false;
              submitBtn.textContent = 'Submit Answer';
            } else {
              // Fallback completion
              window.location.href = `result.html?sessionId=${currentSessionId}`;
            }
          }, 2000);

        } else {
          showToast(data.message || 'Failed to submit answer', 'error');
          submitBtn.disabled = false;
          submitBtn.textContent = 'Submit Answer';
        }
      } catch (err) {
        console.error(err);
        showToast('Network error submitting answer', 'error');
        submitBtn.disabled = false;
        submitBtn.textContent = 'Submit Answer';
      }
    });
  }
});

function updateProgressBar(qNum) {
  const progressBar = document.getElementById('progressBar');
  if (progressBar) {
    // Assuming roughly 3-5 questions per interview, let's animate progress
    const pct = Math.min(qNum * 25, 100);
    progressBar.style.width = `${pct}%`;
  }
}
