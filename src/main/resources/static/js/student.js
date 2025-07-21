const apiBase = '/api';
let selectedStudentId = null;

// Sayfa ilk yüklendiğinde çalışacak fonksiyonlar
document.addEventListener('DOMContentLoaded', () => {
    // 1. Öğrenci seçim menüsünü doldur
    populateStudentSelector();

    // 2. Seçim menüsünde bir değişiklik olduğunda ilgili öğrencinin testlerini yükle
    document.getElementById('student-selector').addEventListener('change', (e) => {
        selectedStudentId = e.target.value;
        const testsSection = document.getElementById('student-tests-section');
        
        if (selectedStudentId) {
            // Testlerin olduğu bölümü görünür yap
            testsSection.style.display = 'grid';
            // Seçilen öğrenci için testleri yükle
            loadAvailableTests(selectedStudentId);
            loadCompletedTests(selectedStudentId);
        } else {
            // "Lütfen seçin" seçeneği seçilirse testleri gizle
            testsSection.style.display = 'none';
        }
    });
});

// Öğrenci listesini çekip seçim menüsünü (dropdown) doldurur
async function populateStudentSelector() {
    const response = await fetch(`${apiBase}/students`);
    const students = await response.json();
    const selector = document.getElementById('student-selector');
    
    students.forEach(student => {
        selector.innerHTML += `<option value="${student.id}">${student.firstName} ${student.lastName} (${student.studentNumber})</option>`;
    });
}

// Seçilen öğrencinin katılabileceği testleri yükler
async function loadAvailableTests(studentId) {
    const response = await fetch(`${apiBase}/students/${studentId}/available-tests`);
    const tests = await response.json();
    const list = document.getElementById('available-tests-list');
    list.innerHTML = ''; 

    if (tests.length > 0) {
        tests.forEach(test => {
            const li = document.createElement('li');
            li.innerHTML = `${test.testName} <button class="start-test-btn" data-testid="${test.id}">Teste Başla</button>`;
            list.appendChild(li);
        });
    } else {
        list.innerHTML = '<li>Katılabileceği yeni bir test bulunmuyor.</li>';
    }
}

// Seçilen öğrencinin tamamladığı testleri yükler
async function loadCompletedTests(studentId) {
    const response = await fetch(`${apiBase}/enrollments/student/${studentId}`);
    const results = await response.json();
    const tableBody = document.getElementById("completed-tests-table-body");
    tableBody.innerHTML = ''; 

    if (results.length > 0) {
        results.forEach(result => {
            tableBody.innerHTML += `
                <tr>
                    <td>${result.testName}</td>
                    <td>${result.finalScore.toFixed(2)}</td>
                </tr>
            `;
        });
    } else {
        tableBody.innerHTML = '<tr><td colspan="2">Henüz tamamlanmış bir test bulunmuyor.</td></tr>';
    }
}

// "Teste Başla" butonlarına olay dinleyici ekler
document.getElementById('available-tests-list').addEventListener('click', async (e) => {
    if (e.target.classList.contains('start-test-btn')) {
        const testId = e.target.dataset.testid;
        
        if (!selectedStudentId) {
            alert("Lütfen önce bir öğrenci seçin!");
            return;
        }
        
        const response = await fetch(`${apiBase}/enrollments?studentId=${selectedStudentId}&testId=${testId}`, {
            method: 'POST'
        });
        const newEnrollment = await response.json();
        
        window.location.href = `take-test.html?enrollmentId=${newEnrollment.id}`;
    }
});