const apiBase = '/api';
let currentStudentId = null;

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    currentStudentId = urlParams.get('studentId');

    if (!currentStudentId) {
        alert("Öğrenci ID'si bulunamadı!");
        window.location.href = 'index.html';
        return;
    }

    loadStudentDetails();
    loadStudentResults();
});

async function loadStudentDetails() {
    const response = await fetch(`${apiBase}/students/${currentStudentId}`);
    if (response.ok) {
        const student = await response.json();
        document.getElementById('student-name-header').textContent = `Öğrenci: ${student.firstName} ${student.lastName}`;
    } else {
        document.getElementById('student-name-header').textContent = 'Öğrenci Bulunamadı';
    }
}

async function loadStudentResults() {
    const response = await fetch(`${apiBase}/enrollments/student/${currentStudentId}`);
    const results = await response.json();
    const tableBody = document.querySelector("#results-table tbody");
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
        tableBody.innerHTML = '<tr><td colspan="2">Bu öğrencinin tamamladığı bir test bulunmuyor.</td></tr>';
    }
}