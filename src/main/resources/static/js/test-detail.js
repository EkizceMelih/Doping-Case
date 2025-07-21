const apiBase = '/api';
let currentTestId = null;

// Sayfa yüklendiğinde çalışacak ana fonksiyon
document.addEventListener('DOMContentLoaded', () => {
    // 1. URL'den test ID'sini al
    const urlParams = new URLSearchParams(window.location.search);
    currentTestId = urlParams.get('testId');

    if (!currentTestId) {
        window.location.href = 'index.html'; // ID yoksa anasayfaya dön
        return;
    }

    // 2. Test detaylarını ve sorularını yükle
    loadTestDetails();

    // 3. Yeni soru ekleme formuna olay dinleyici ekle
    document.getElementById('question-form').addEventListener('submit', handleAddQuestion);
});

// Test detaylarını, sorularını ve cevaplarını API'den çeken fonksiyon
async function loadTestDetails() {
    const response = await fetch(`${apiBase}/tests/${currentTestId}`);
    if (!response.ok) {
        document.getElementById('test-name-header').textContent = 'Test Bulunamadı!';
        return;
    }
    const test = await response.json();

    // Sayfa başlığını test adıyla güncelle
    document.getElementById('test-name-header').textContent = `Test: ${test.testName}`;

    // Soruları ve cevapları ekrana çiz
    const questionsContainer = document.getElementById('questions-container');
    questionsContainer.innerHTML = ''; // Konteyneri temizle

    if (test.questions && test.questions.length > 0) {
        test.questions.forEach(question => {
            const questionBlock = document.createElement('div');
            questionBlock.className = 'question-block';
            
            // Mevcut cevapları (şıklar) listele
            let answersHtml = '<ul>';
            if (question.answers && question.answers.length > 0) {
                question.answers.forEach(answer => {
                    answersHtml += `<li>${answer.answerText} ${answer.correct ? '<strong>(Doğru Cevap)</strong>' : ''}</li>`;
                });
            } else {
                answersHtml += '<li>Henüz cevap eklenmemiş.</li>';
            }
            answersHtml += '</ul>';

            // Soru ve cevap ekleme formunu oluştur
            questionBlock.innerHTML = `
                <h4>Soru: ${question.questionText}</h4>
                ${answersHtml}
                <form class="answer-form" data-question-id="${question.id}">
                    <input type="text" name="answerText" placeholder="Yeni cevap metni" required>
                    <label>
                        <input type="checkbox" name="isCorrect"> Doğru Cevap mı?
                    </label>
                    <button type="submit">Cevabı Ekle</button>
                </form>
            `;
            questionsContainer.appendChild(questionBlock);
        });
    } else {
        questionsContainer.innerHTML = '<p>Bu teste henüz soru eklenmemiş.</p>';
    }

    // Her bir cevap ekleme formuna olay dinleyici ekle
    document.querySelectorAll('.answer-form').forEach(form => {
        form.addEventListener('submit', handleAddAnswer);
    });
}

// Yeni soru ekleme formunu yöneten fonksiyon
async function handleAddQuestion(e) {
    e.preventDefault();
    const form = e.target;
    const body = JSON.stringify({
        questionText: form.questionText.value
    });

    await fetch(`${apiBase}/tests/${currentTestId}/questions`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: body
    });

    form.reset();
    await loadTestDetails(); // Soru eklendikten sonra listeyi yenile
}

// Yeni cevap ekleme formunu yöneten fonksiyon
async function handleAddAnswer(e) {
    e.preventDefault();
    const form = e.target;
    const questionId = form.dataset.questionId;
    const body = JSON.stringify({
        answerText: form.answerText.value,
        correct: form.isCorrect.checked
    });

    // --- BU SATIRI GÜNCELLEYİN ---
    // ESKİ HALİ: await fetch(`${apiBase}/questions/${questionId}/answers`, {
    // YENİ HALİ:
    await fetch(`${apiBase}/tests/questions/${questionId}/answers`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: body
    });

    form.reset();
    await loadTestDetails(); 
}