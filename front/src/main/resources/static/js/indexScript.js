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

// Находим элементы на странице
const riverSelect = document.getElementById("river");
const gesSelect = document.getElementById("ges");
const dateSelect = document.getElementById("input_date")

// Обработчик изменения выбора реки
riverSelect.addEventListener("change", function () {
    const selectedRiver = this.value;

    // Очищаем список ГЭС
    gesSelect.innerHTML = "";
    gesSelect.disabled = true;

    // Если река не выбрана
    if (!selectedRiver) {
        gesSelect.innerHTML = '<option value="">-- Сначала выберите реку --</option>';
        return;
    }

    // Получаем ГЭС для выбранной реки
    const gess = gesByRivers[selectedRiver];

    // Если ГЭС есть, заполняем список
    if (gess && gess.length > 0) {
        gess.forEach(ges => {
            // const option = new Option(ges, ges.toLowerCase());
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
        // river: $("#river").val(),
        river: $("#river").val().replace(/_/g, ' '),
        ges: $("#ges").val(),
        date: $("#input_date").val()
    };

    console.log("Отправка данных:", requestData);

    // Вариант 1: POST запрос с JSON в теле
    fetch('/api/request', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (!response.ok) throw new Error('Ошибка сети');
            return response.text();
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

    // fetch('/api/requestToBD', {
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

            // Модифицированный код обработки редиректа
            const redirectUrl = new URL(data.redirectUrl, 'http://localhost:8081');
            console.log("Перенаправляю на:", redirectUrl.href);  // Переходим на страницу графика
            window.location.href = redirectUrl.href;
        })

        .catch(error => {
            console.error("Ошибка:", error);
            alert("Ошибка при отправке данных: " + error.message);
        });
}

async function loadCurrentParams() {
    // 1. Подготовка URL с учетом другого порта (8083)
    const apiUrl = 'http://localhost:8083/api/crawler/params';

    // 2. Добавляем индикатор загрузки на кнопку
    const refreshBtn = document.querySelector('button[onclick="loadCurrentParams()"]');
    const originalBtnText = refreshBtn.textContent;
    refreshBtn.disabled = true;
    refreshBtn.textContent = 'Загрузка...';

    try {
        console.log("Отправка запроса параметров на:", apiUrl);

        // 3. Отправляем GET-запрос (аналогично send_requestToDB, но без тела)
        const response = await fetch(apiUrl, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                // Добавляем CORS заголовки при необходимости
                'Accept': 'application/json'
            },
            credentials: 'include' // Если используются куки/сессии
        });

        // 4. Проверяем ответ
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // 5. Парсим JSON
        const data = await response.json();
        console.log("Получены параметры:", data);

        // 6. Обновляем интерфейс
        const setValue = (id, value) => {
            const element = document.getElementById(id);
            if (element) element.textContent = value || 'Не указано';
        };

        const setInputValue = (id, value) => {
            const input = document.getElementById(id);
            if (input) input.value = value || '';
        };

        // Для отображения
        setValue('currentRiver', data.river);
        setValue('currentGes', data.ges);
        setValue('currentDate', data.date);

        // Для формы
        setInputValue('river', data.river);
        setInputValue('ges', data.ges);
        setInputValue('date', formatDateForInput(data.date));

        console.log("Параметры успешно обновлены");

    } catch (error) {
        console.error('Ошибка при загрузке параметров:', error);

        // 7. Показываем пользователю ошибку
        alert('Ошибка при загрузке параметров: ' + error.message);

        // Устанавливаем значения ошибки
        document.getElementById('currentRiver').textContent = 'Ошибка';
        document.getElementById('currentGes').textContent = 'Ошибка';
        document.getElementById('currentDate').textContent = 'Ошибка';
    } finally {
        // 8. Восстанавливаем кнопку
        refreshBtn.disabled = false;
        refreshBtn.textContent = originalBtnText;
    }
}

// Вспомогательная функция для форматирования даты
function formatDateForInput(dateString) {
    if (!dateString) return '';
    try {
        return new Date(dateString).toISOString().split('T')[0];
    } catch (e) {
        console.warn('Ошибка форматирования даты:', e);
        return dateString; // Возвращаем как есть, если не удалось преобразовать
    }
}

// async function loadCurrentParams() {
//     try {
//         // 1. Отправляем GET-запрос к API
//         const response = await fetch('http://localhost:8082/api/crawler/params', {
//             method: 'GET',
//             headers: {
//                 'Content-Type': 'application/json',
//             }
//         });
//
//         // 2. Проверяем статус ответа
//         if (!response.ok) {
//             throw new Error(`HTTP error! status: ${response.status}`);
//         }
//
//         // 3. Парсим JSON ответ
//         const data = await response.json();
//         console.log("Получены параметры:", data);
//
//         // 4. Обновляем DOM-элементы
//         document.getElementById('currentRiver').textContent = data.river || 'Не указано';
//         document.getElementById('currentGes').textContent = data.ges || 'Не указано';
//         document.getElementById('currentDate').textContent = data.date || 'Не указано';
//
//         // 5. Заполняем форму (если элементы существуют)
//         if (document.getElementById('river')) {
//             document.getElementById('river').value = data.river || '';
//         }
//         if (document.getElementById('ges')) {
//             document.getElementById('ges').value = data.ges || '';
//         }
//         if (document.getElementById('date')) {
//             document.getElementById('date').value = data.date || '';
//         }
//
//         // 6. Уведомление об успехе
//         console.log("Параметры успешно обновлены");
//
//     } catch (error) {
//         console.error('Ошибка при загрузке параметров:', error);
//
//         // 7. Показываем пользователю сообщение об ошибке
//         alert('Не удалось загрузить параметры. Проверьте консоль для подробностей.');
//
//         // 8. Можно установить значения по умолчанию при ошибке
//         document.getElementById('currentRiver').textContent = 'Ошибка загрузки';
//         document.getElementById('currentGes').textContent = 'Ошибка загрузки';
//         document.getElementById('currentDate').textContent = 'Ошибка загрузки';
//     }
// }


// // Глобальная функция для кнопки краулера
// window.startCrawler = async function() {
//     console.log('Функция startCrawler вызвана'); // Логирование
//
//     try {
//         // 1. Подготовка запроса
//         const apiUrl = 'http://localhost:8082/api/crawler/manual-start';
//         const options = {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json',
//                 'Accept': 'application/json'
//             },
//             // credentials: 'include',
//             credentials: 'same-origin',
//             body: JSON.stringify({}) // Пустое тело запроса
//         };
//
//         console.log('Отправка запроса на:', apiUrl);
//
//         // 2. Отправка запроса
//         const response = await fetch(apiUrl, options);
//
//         console.log('Получен ответ. Статус:', response.status);
//
//         // 3. Обработка ответа
//         if (!response.ok) {
//             const errorText = await response.text();
//             throw new Error(`Ошибка сервера ${response.status}: ${errorText}`);
//         }
//
//         // const result = await response.json();
//         const result = await response.text();
//
//         // 4. Уведомление пользователя
//         alert(`Краулинг успешно запущен: ${JSON.stringify(result, null, 2)}`);
//         console.log('Результат:', result);
//
//     } catch (error) {
//         // 5. Обработка ошибок
//         console.error('Произошла ошибка:', error);
//         alert(`Ошибка при запуске краулинга: ${error.message}`);
//     }
// };


document.getElementById('input_date').max = new Date().toISOString().split('T')[0];
document.getElementById('input_dateStart').max = new Date().toISOString().split('T')[0];
document.getElementById('input_dateFinish').max = new Date().toISOString().split('T')[0];


// Дополнительно: сохраняем выбранную ГЭС в переменную
let selectedGes = "";
gesSelect.addEventListener("change", function () {
    selectedGes = this.value;
    console.log("Выбрана ГЭС:", selectedGes);
});

// Дополнительно: сохраняем выбранную Реку в переменную
let selectedRiver = "";
riverSelect.addEventListener("change", function () {
    selectedRiver = this.value;
    console.log("Выбрана Река:", selectedRiver);
});

// Дополнительно: сохраняем выбранную Дату в переменную
let selectedDate = "";
dateSelect.addEventListener("change", function () {
    selectedDate = this.value;
    console.log("Выбрана Дата:", selectedDate);
});