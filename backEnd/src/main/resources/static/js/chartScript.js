// Функция для декодирования URL-параметров
function getUrlParams() {
    const params = new URLSearchParams(window.location.search);
    return {
        river: decodeURIComponent(params.get('river') || ''),
        ges: decodeURIComponent(params.get('ges') || ''),
        dateStart: params.get('dateStart') || '',
        dateFinish: params.get('dateFinish') || ''
    };
}

// Отображаем параметры на странице
document.addEventListener('DOMContentLoaded', () => {
    const params = getUrlParams();

    document.getElementById('river').textContent = params.river || 'Не указано';
    document.getElementById('ges').textContent = params.ges || 'Не указано';
    document.getElementById('dateStart').textContent = params.dateStart || 'Не указано';
    document.getElementById('dateFinish').textContent = params.dateFinish || 'Не указано';
});

// Глобальные переменные
let waterLevelChart = null;
const statusEl = document.getElementById('status');
const errorEl = document.getElementById('error');

// Форматирование даты
function formatDateTime(date) {
    return new Date(date).toLocaleString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
}

// Обновление статуса
function updateStatus(message, isError = false) {
    statusEl.textContent = message;
    if (isError) {
        statusEl.style.color = '#d32f2f';
    } else {
        statusEl.style.color = '#5f6368';
    }
    errorEl.style.display = 'none';
}

// Показать ошибку
function showError(message) {
    errorEl.textContent = message;
    errorEl.style.display = 'block';
}

// Генерация массива дат между начальной и конечной датой
function generateDateRange(startDate, endDate) {
    const dates = [];
    let currentDate = new Date(startDate);
    const lastDate = new Date(endDate);

    while (currentDate <= lastDate) {
        dates.push(new Date(currentDate));
        currentDate.setDate(currentDate.getDate() + 1);
    }

    return dates;
}

// Загрузка данных с сервера
async function fetchData() {
    try {
        updateStatus('Загрузка данных с сервера...');

        const params = getUrlParams();
        const queryParams = new URLSearchParams();

        if (params.river) queryParams.append('river', params.river);
        if (params.ges) queryParams.append('ges', params.ges);
        if (params.dateStart) queryParams.append('dateStart', params.dateStart);
        if (params.dateFinish) queryParams.append('dateFinish', params.dateFinish);

        // const response = await fetch(`/chart-data?${queryParams.toString()}`, {
        // Изменяем URL на полный с портом 8080
        // const response = await fetch(`http://localhost:8080/chart-data?${queryParams.toString()}`, {
        const response = await fetch(`/chart-data?${queryParams.toString()}`, {
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка сервера: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();

        if (!Array.isArray(data)) {
            throw new Error('Некорректный формат данных');
        }

        updateStatus(`Загружено ${data.length} записей`);
        renderChart(data);

    } catch (error) {
        console.error('Ошибка:', error);
        updateStatus('Ошибка при загрузке данных', true);
        showError(error.message);
    }
}

// Отрисовка/обновление графика с заполнением пропусков нулями
function renderChart(data) {
    const ctx = document.getElementById('waterLevelChart').getContext('2d');
    const params = getUrlParams();

    // Определяем диапазон дат
    const startDate = params.dateStart ? new Date(params.dateStart) : new Date();
    const endDate = params.dateFinish ? new Date(params.dateFinish) : new Date();

    // Корректируем время
    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(23, 59, 59, 999);

    // Генерируем полный диапазон дат
    const allDates = generateDateRange(startDate, endDate);

    // Создаем карту существующих данных
    const dataMap = {};
    if (Array.isArray(data)) {
        data.forEach(item => {
            const date = new Date(item.date);
            const dateStr = date.toISOString().split('T')[0];
            dataMap[dateStr] = item.level;
        });
    }

    // Формируем данные для графика
    const chartDataPoints = allDates.map(date => {
        const dateStr = date.toISOString().split('T')[0];
        return {
            x: date,
            y: dataMap[dateStr] !== undefined ? dataMap[dateStr] : 0
        };
    });

    const chartData = {
        datasets: [{
            label: 'Уровень воды (м)',
            data: chartDataPoints,
            borderColor: '#4285f4',
            backgroundColor: 'rgba(66, 133, 244, 0.1)',
            borderWidth: 2,
            tension: 0.2,
            pointRadius: function (context) {
                // Всегда показываем точки, даже для нулевых значений
                return 4;
            },
            pointHoverRadius: 6,
            pointBackgroundColor: '#4285f4',
            pointBorderColor: '#fff',
            fill: true,
            spanGaps: true // Разрешаем разрывы в данных
        }]
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            tooltip: {
                callbacks: {
                    title: (context) => {
                        return moment(context[0].parsed.x).format('DD.MM.YYYY');
                    },
                    label: (context) => {
                        return `Уровень: ${context.parsed.y.toFixed(2)} м`;
                    }
                }
            },
            legend: {
                position: 'top',
                labels: {
                    boxWidth: 12,
                    padding: 20,
                    usePointStyle: true,
                    pointStyle: 'circle'
                }
            }
        },
        scales: {
            x: {
                type: 'time',
                time: {
                    parser: 'YYYY-MM-DD',
                    tooltipFormat: 'DD.MM.YYYY',
                    unit: 'day',
                    displayFormats: {
                        day: 'DD.MM.YYYY'
                    }
                },
                title: {
                    display: true,
                    text: 'Дата',
                    font: {
                        weight: 'bold',
                        size: 14
                    },
                    padding: {top: 10, bottom: 10}
                },
                grid: {
                    drawOnChartArea: false,
                    color: '#e0e0e0'
                },
                ticks: {
                    maxRotation: 45,
                    minRotation: 45
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'Уровень (м)',
                    font: {
                        weight: 'bold',
                        size: 14
                    },
                    padding: {top: 10, bottom: 10}
                },
                ticks: {
                    callback: (value) => `${value.toFixed(2)} м`
                },
                grid: {
                    color: '#f1f1f1'
                },
                // Убедимся, что 0 всегда включен в шкалу
                beginAtZero: true,
                min: 0
            }
        },
        interaction: {
            intersect: false,
            mode: 'index'
        },
        elements: {
            point: {
                // Всегда показываем точки
                radius: function (context) {
                    return context.datasetIndex === 0 ? 4 : 0;
                },
                hoverRadius: function (context) {
                    return context.datasetIndex === 0 ? 6 : 0;
                }
            }
        }
    };

    if (waterLevelChart) {
        waterLevelChart.data = chartData;
        waterLevelChart.options = options;
        waterLevelChart.update();
    } else {
        waterLevelChart = new Chart(ctx, {
            type: 'line',
            data: chartData,
            options: options
        });
    }
}

// Инициализация
document.addEventListener('DOMContentLoaded', () => {
    fetchData();
});
