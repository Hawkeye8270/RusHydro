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
riverSelect.addEventListener("change", function() {
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

//
// function send_request() {
//     const requestData = {
//         river: $("#river").val().replace(/_/g, ' '),
//         ges: $("#ges").val(),
//         date: $("#input_date").val()
//     };
//
//     console.log("Отправка данных:", requestData);
//
//     fetch('/api/request', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(requestData)
//     })
//         .then(response => {
//             if (!response.ok) throw new Error('Ошибка сети');
//             return response.json(); // Изменено с text() на json(), если сервер возвращает JSON
//         })
//         .then(data => {
//             console.log("Успех:", data);
//
//             // Создаем всплывающее окно с сообщением
//             // const message = `Данные получены в количестве ${data.count || 'неизвестном'}`;
//             const message = `Данные получены в количестве ${data.count || 'неизвестном'}`;
//             alert(message); // Или используйте более красивый модальный диалог
//
//             setTimeout(() => location.reload(), 30000);
//         })
//         .catch(error => {
//             console.error("Ошибка:", error);
//             alert("Произошла ошибка при получении данных");
//         });
// }



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

    fetch('/api/requestToBD', {
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
            console.log("Успех, перенаправляю на:", data.redirectUrl);
            window.location.href = data.redirectUrl;  // Переходим на страницу графика
        })
        .catch(error => {
            console.error("Ошибка:", error);
            alert("Ошибка при отправке данных: " + error.message);
        });
}

// 01/07 ????????????????????????
// document.getElementById('input_date').max = new Date().toISOString().split('T')[0];
// document.getElementById('input_dateStart').max = new Date().toISOString().split('T')[0];
// document.getElementById('input_dateFinish').max = new Date().toISOString().split('T')[0];

// function send_requestToDB() {
//
//     const requestDataToBD = {
//         // river: $("#river").val(),
//         river: $("#river").val().replace(/_/g, ' '),
//         ges: $("#ges").val(),
//         dateStart: $("#input_dateStart").val(),
//         dateFinish: $("#input_dateFinish").val()
//     };
//
//     console.log("Отправка данных:", requestDataToBD);
//
//     // Вариант 1: POST запрос с JSON в теле
//     fetch('/api/requestToBD', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(requestDataToBD)
//     })
//         .then(response => {
//             if (!response.ok) throw new Error('Ошибка сети');
//             return response.text();
//         })
//         .then(data => {
//             console.log("Успех:", data);
//             setTimeout(() => location.reload(), 30000);
//         })
//         .catch(error => {
//             console.error("Ошибка:", error);
//         });
//
//     // Вариант 2: GET запрос с параметрами в URL
//     // const params = new URLSearchParams({
//     //     river: $("#river").val(),
//     //     ges: $("#ges").val(),
//     //     date: $("#input_date").val()
//     // });
//     // // window.location.href = `/?${params.toString()}`;
// }



// Дополнительно: сохраняем выбранную ГЭС в переменную
let selectedGes = "";
gesSelect.addEventListener("change", function() {
    selectedGes = this.value;
    console.log("Выбрана ГЭС:", selectedGes);
});

// Дополнительно: сохраняем выбранную Реку в переменную
let selectedRiver = "";
riverSelect.addEventListener("change", function() {
    selectedRiver = this.value;
    console.log("Выбрана Река:", selectedRiver);
});

// Дополнительно: сохраняем выбранную Дату в переменную
let selectedDate = "";
dateSelect.addEventListener("change", function() {
    selectedDate = this.value;
    console.log("Выбрана Дата:", selectedDate);
});