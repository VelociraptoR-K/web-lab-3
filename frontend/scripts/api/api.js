import { errorHandler } from "../shared/errors.js";
import { apiFetch } from "./apiWrapper.js";

const apiRoute = "http://localhost:8080/api/requests/"
// const apiRoute = "http://192.168.0.114:8080/api/requests/";

export async function getAllStudentsFiltered(params) {
    let response;
    try{
        response = await apiFetch(apiRoute + "?" + params);
    } catch(error) {
        throw new Error(`Ошибка при запросе к API`);
    }

    if (!response.ok) {
        await errorHandler(response);
    }

    const data = await response.json();
    return {
        students: data.students,
        total_pages: data.totalPages,
        studentsCount: data.studentsCount
    }
}

export async function getAllStudentsQuery(params) {
    let response;
    try {
        response = await apiFetch(apiRoute, {
            method: "QUERY",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(params),
        });
    } catch(error) {
        throw new Error(`Ошибка при запросе к API`)
    }

    if (!response.ok) await errorHandler(response);

    const data = await response.json();
    return {
        students: data.students,
        total_pages: data.totalPages,
        studentsCount: data.studentsCount
    }
}

export async function getStudentById(id) {
    let response;
    try {
        response = await apiFetch(apiRoute + id);
    } catch(error) {
        throw new Error(`Ошибка при запросе к API`);
    }  

    if (!response.ok) {
        await errorHandler(response);
        }
    const data = await response.json();
    return data.student;
}

export async function createStudent(student) {
    let response;
    try{
        response = await apiFetch(apiRoute, {
            method: "POST",
            body: JSON.stringify(student),
            headers: {
                "Content-Type": "application/json",
            },
        });
    } catch(error) {
        throw new Error(`Ошибка при запросе к API`);
    }
    
    if (!response.ok) {
        await errorHandler(response);
    }
}

export async function editStudent(id, changedFields) {
    let response;
    try {
        response = await apiFetch(apiRoute + id, {
            method: "PATCH",
            body: JSON.stringify(changedFields),
            headers: {
                "Content-Type": "application/json",
            },
        });
    } catch(error) {
        throw new Error(`Ошибка при запросе к API`);
    }

    if (!response.ok) {
        await errorHandler(response);
    }
}

export async function deleteStudent(id) {
    let response;
    try {
        response = await apiFetch(apiRoute + id, {
            method: "DELETE"
        });
    } catch(error) {
        throw new Error(`Ошибка при запросе к API`);
    }
    if (!response.ok) {
        await errorHandler(response);
    }
}