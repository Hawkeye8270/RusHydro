// Данные: ГЭС для каждой реки
const gesByRivers = {
    Ангара: ["Богучанская", "Братская", "Иркутская", "Усть-Илимская"],
    Зея: ["Зейская"],
    Бурея: ["Бурейская", "Нижне-Бурейская"],
    Колыма: ["Колымская", "Усть-Среднеканская"],
    Волга: ["Волжская", "Жигулевская", "Нижегородская", "Рыбинская", "Саратовская", "Угличская", "Чебоксарская"],
    Кама: ["Воткинская", "Камская", "Нижнекамская"],
    Енисей: ["Майнская", "Саяно-Шушенская"],
    Обь: ["Новосибирская"],
    Аварское_Койсу: ["Ирганайская"],
    Сулак: ["Чиркейская"],
    Вилюй: ["Вилюйская"]
};

const riverSelect = document.getElementById("river");
const gesSelect = document.getElementById("ges");
const dateSelect = document.getElementById("input_date")

riverSelect.addEventListener("change", function () {
    const selectedRiver = this.value;

    gesSelect.innerHTML = "";
    gesSelect.disabled = true;

    if (!selectedRiver) {
        gesSelect.innerHTML = '<option value="">-- Сначала выберите реку --</option>';
        return;
    }

    const gess = gesByRivers[selectedRiver];

    if (gess && gess.length > 0) {
        gess.forEach(ges => {
            const option = new Option(ges, ges);
            gesSelect.add(option);
        });
        gesSelect.disabled = false;
    } else {
        gesSelect.innerHTML = '<option value="">-- Нет данных о ГЭС --</option>';
    }
});


function send_request() {

    const requestData = {
        river: $("#river").val().replace(/_/g, ' '),
        ges: $("#ges").val(),
        date: $("#input_date").val()
    };

    console.log("Отправка данных:", requestData);

    fetch('http://localhost:8082/api/crawler/request', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData)
    })

        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.json();  // Парсим JSON, а не текст
        })
        .then(data => {
            console.log("Успех:", data);
            setTimeout(() => location.reload(), 30000);
        })
        .catch(error => {
            console.error("Ошибка:", error);
        });
}

function send_requestToDB() {
    const requestDataToBD = {
        river: $("#river").val().replace(/_/g, ' '),
        ges: $("#ges").val(),
        dateStart: $("#input_dateStart").val(),
        dateFinish: $("#input_dateFinish").val()
    };

    console.log("Отправка данных:", requestDataToBD);

    fetch('http://localhost:8081/api/requestToBD', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestDataToBD)
    })
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.json();  // Парсим JSON, а не текст
        })
        .then(data => {
            console.log("Получен ответ:", data);

            const redirectUrl = new URL(data.redirectUrl, 'http://localhost:8081');
            console.log("Перенаправляю на:", redirectUrl.href);  // Переходим на страницу графика
            window.location.href = redirectUrl.href;
        })

        .catch(error => {
            console.error("Ошибка:", error);
            alert("Ошибка при отправке данных: " + error.message);
        });
}

function loadCurrentParams() {
    const button = document.getElementById('loadCurrentParams');
    const originalText = button.textContent;

    button.disabled = true;
    button.textContent = 'Загрузка...';

    console.log("Запрашиваю текущие параметры...");

    fetch('http://localhost:8082/api/crawler/params', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },

    })
        .then(response => {
            if (!response.ok) throw new Error(`Ошибка HTTP! Статус: ${response.status}`);
            return response.json();
        })
        .then(data => {
            console.log("Получены параметры:", data);

            // Форматируем дату из массива [2020,3,1] в строку "01.03.2020"
            const formattedDate = data.date ?
                `${String(data.date[2]).padStart(2, '0')}.${String(data.date[1]).padStart(2, '0')}.${data.date[0]}` :
                'Не указано';

            document.getElementById('currentRiver').textContent = data.river || 'Не указано';
            document.getElementById('currentGes').textContent = data.ges || 'Не указано';
            document.getElementById('currentDate').textContent = formattedDate;

            console.log("Интерфейс успешно обновлен");
        })
        .catch(error => {
            console.error("Ошибка при загрузке параметров:", error);

            document.getElementById('currentRiver').textContent = 'Ошибка';
            document.getElementById('currentGes').textContent = 'Ошибка';
            document.getElementById('currentDate').textContent = 'Ошибка';

            alert(`Ошибка при загрузке параметров: ${error.message}\nПроверьте:\n1. Сервер на порту 8082\n2. Консоль разработчика (F12)`);
        })
        .finally(() => {
            button.disabled = false;
            button.textContent = originalText;
        });
}

function formatDateForInput(dateString) {
    if (!dateString) return '';
    try {
        return new Date(dateString).toISOString().split('T')[0];
    } catch (e) {
        console.warn('Ошибка форматирования даты:', e);
        return dateString;
    }
}

document.getElementById('input_date').max = new Date().toISOString().split('T')[0];
document.getElementById('input_dateStart').max = new Date().toISOString().split('T')[0];
document.getElementById('input_dateFinish').max = new Date().toISOString().split('T')[0];


let selectedGes = "";
gesSelect.addEventListener("change", function () {
    selectedGes = this.value;
    console.log("Выбрана ГЭС:", selectedGes);
});

let selectedRiver = "";
riverSelect.addEventListener("change", function () {
    selectedRiver = this.value;
    console.log("Выбрана Река:", selectedRiver);
});

let selectedDate = "";
dateSelect.addEventListener("change", function () {
    selectedDate = this.value;
    console.log("Выбрана Дата:", selectedDate);
});