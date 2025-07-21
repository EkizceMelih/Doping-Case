const apiBase = '/api';
let currentEnrollmentId = null;
let currentStudentId = null; // Öğrencinin ID'sini saklamak için genel bir değişken

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    currentEnrollmentId = urlParams.get('enrollmentId');

    if (!currentEnrollmentId) {
        alert('Geçerli bir test katılımı bulunamadı!');
        window.location.href = 'index.html'; // Hata durumunda anasayfaya yönlendir
        return;
    }
    
    loadQuiz();

    document.getElementById('quiz-form').addEventListener('submit', handleSubmitQuiz);
});

// Testi ve sorularını yükleyip ekrana çizer
async function loadQuiz() {
    const response = await fetch(`${apiBase}/enrollments/${currentEnrollmentId}`);
    
    if (!response.ok) {
        document.getElementById('test-name-header').textContent = 'Test yüklenirken bir hata oluştu.';
        return;
    }
    
    const quiz = await response.json(); // Gelen veri artık QuizDTO formatında
    
    // Geri dönüş linki için öğrenci ID'sini kaydediyoruz
    currentStudentId = quiz.studentId; 
    
    document.getElementById('test-name-header').textContent = quiz.testName;
    
    const quizContainer = document.getElementById('quiz-container');
    quizContainer.innerHTML = '';

    quiz.questions.forEach((question, index) => {
        let answersHtml = '';
        question.answers.forEach(answer => {
            answersHtml += `
                <div>
                    <input type="radio" name="question-${question.id}" id="answer-${answer.id}" value="${answer.id}" required>
                    <label for="answer-${answer.id}">${answer.answerText}</label>
                </div>
            `;
        });

        quizContainer.innerHTML += `
            <div class="question-block">
                <h4>${index + 1}. Soru: ${question.questionText}</h4>
                <div class="answers" data-question-id="${question.id}">
                    ${answersHtml}
                </div>
            </div>
        `;
    });
}

// "Testi Bitir" butonuna basıldığında çalışır
async function handleSubmitQuiz(e) {
    e.preventDefault();

    // 1. Tüm seçilen cevapları bul ve API'ye gönder
    const answerPromises = [];
    const answerDivs = document.querySelectorAll('.answers');

    answerDivs.forEach(div => {
        const questionId = div.dataset.questionId;
        const selectedAnswer = div.querySelector(`input[name="question-${questionId}"]:checked`);
        
        if (selectedAnswer) {
            const chosenAnswerId = selectedAnswer.value;
            const promise = fetch(`${apiBase}/enrollments/${currentEnrollmentId}/answers?questionId=${questionId}&chosenAnswerId=${chosenAnswerId}`, {
                method: 'POST'
            });
            answerPromises.push(promise);
        }
    });
    
    // Tüm cevapların kaydedilmesini bekle
    await Promise.all(answerPromises);

    // 2. Testi sonlandır ve skoru hesapla
    const finalizeResponse = await fetch(`${apiBase}/enrollments/${currentEnrollmentId}/finalize`, {
        method: 'POST'
    });
    const result = await finalizeResponse.json();
    
    // 3. Sonucu ekranda göster
    document.getElementById('quiz-form').style.display = 'none';
    document.getElementById('result-container').style.display = 'block';
    document.getElementById('final-score').textContent = result.finalScore.toFixed(2);
    
    // Önceden kaydettiğimiz öğrenci ID'sini kullanarak doğru geri dönüş linkini oluştur
    if (currentStudentId) {
        document.querySelector("#result-container a").href = `student.html?studentId=${currentStudentId}`;
    } else {
        // ID alınamadıysa genel bir sayfaya yönlendir
        document.querySelector("#result-container a").href = `index.html`;
    }
}