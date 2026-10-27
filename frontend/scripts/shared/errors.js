

export function showError(error) {
    const errorContainer = document.querySelector(".error-container");
    const errorText = errorContainer.querySelector("p");

    errorText.textContent = error.message || "Не удалось загрузить студентов";
    errorContainer.hidden = false;
}


export async function errorHandler(response) {
    const data = await response.json().catch(() => null);

    switch (response.status) {
        case 422:
            const fieldNames = {
                patronymic: "Отчество",
                name: "Имя",
                surname: "Фамилия",
                group: "Группа",
                isuId: "ИСУ ID",
                dormitoryNumber: "Номер общежития",
                room: "Комната",
                moveInDate: "Дата заселения"
            };

            let details = [];
            if (Array.isArray(data?.details)) {
                details = data.details.map(error => {
                    return `${fieldNames[error.fieldName]}: ${error.message}`;
                })
            }

            const message = data?.message ?? `Произошла ошибка (${response.status})`;
            throw new Error([message, ...details].join("\n"));
        case 409:
            throw new Error(`Ошибка конфликта данных (409):\n${data.message}`);
        case 404:
            throw new Error(`Ошибка не найдено (404):\n${data.message}`);
        case 400:
            throw new Error(`Ошибка при запросе (400):\n${data.message}`);
        case 500:
            throw new Error(`Ошибка на сервере (500):\n${data.message}`);
        default:
            throw new Error(`Произошла ошибка (${response.status})`);
    }
}