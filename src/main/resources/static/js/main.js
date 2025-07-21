const apiBase = '/api';

// Öğrencileri yükle ve tabloya bas
async function loadStudents() {
    try {
        const response = await fetch(`${apiBase}/students`);
        if (!response.ok) {
            console.error("Öğrenci listesi alınamadı. Sunucu hatası:", response.status);
            return;
        }
        const students = await response.json();
        
        const studentTableBody = document.getElementById('student-list-body');
        studentTableBody.innerHTML = ''; 

        students.forEach(student => {
            studentTableBody.innerHTML += `
                <tr>
                    <td><a href="student-detail.html?studentId=${student.id}">${student.firstName} ${student.lastName}</a></td>
                    <td>(${student.studentNumber})</td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("Öğrenciler yüklenirken bir ağ hatası oluştu:", error);
    }
}

// Testleri yükle ve tabloya bas
async function loadTests() {
    try {
        const response = await fetch(`${apiBase}/tests`);
        if (!response.ok) {
            // Hata durumunda kullanıcıya bilgi ver
            const testTableBody = document.getElementById('test-list-body');
            testTableBody.innerHTML = '<tr><td colspan="1">Testler yüklenemedi. Backend hatası olabilir.</td></tr>';
            console.error("Test listesi alınamadı. Sunucu hatası:", response.status);
            return;
        }
        const tests = await response.json();
        
        const testTableBody = document.getElementById('test-list-body');
        testTableBody.innerHTML = ''; 
        
        tests.forEach(test => {
            testTableBody.innerHTML += `
                <tr>
                    <td><a href="test-detail.html?testId=${test.id}">${test.testName}</a></td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("Testler yüklenirken bir ağ hatası oluştu:", error);
    }
}

// Sayfa yüklendiğinde verileri çek
window.addEventListener('DOMContentLoaded', () => {
    loadStudents();
    loadTests();

    // Öğrenci ekleme formu
    document.getElementById('student-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const body = JSON.stringify({
            firstName: form.firstName.value,
            lastName: form.lastName.value,
            studentNumber: form.studentNumber.value
        });

        await fetch(`${apiBase}/students`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });

        form.reset();
        await loadStudents(); // Listeyi yenile
    });

    // Test oluşturma formu
    document.getElementById('test-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const body = JSON.stringify({ testName: form.testName.value });

        await fetch(`${apiBase}/tests`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: body
        });

        form.reset();
        
        // --- EKSİK OLAN KRİTİK SATIR BURADAYDI ---
        await loadTests(); // Listeyi yenile
    });
});