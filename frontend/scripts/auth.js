import { apiFetch } from "./apiWrapper.js";
import { errorHandler } from "./errors.js";

const authUrl = "http://127.0.0.1:8080/api/auth";

let accessToken = null;

export function getAccessToken() {
    return accessToken;
}

async function readResponse(response) {
    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(
            data?.message ?? `Ошибка (${response.status})`
        );
    }

    return data;
}

export async function login(username, password) {
    const response = await fetch(`${authUrl}/login`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    const data = await readResponse(response);
    accessToken = data.accessToken;
}

export async function register(username, email, password) {
    const response = await fetch(`${authUrl}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password })
    });

    return readResponse(response);
}

export async function refresh() {
    const response = await fetch(`${authUrl}/refresh`, {
        method: "POST",
        credentials: "include"
    });

    if (response.status === 401) {
        accessToken = null;
        return false;
    }

    const data = await readResponse(response);
    accessToken = data.accessToken;
    return true;
}

export async function getCurrentUser() {
    const response = await apiFetch(
        "http://localhost:8080/api/auth/me"
    );

    if (!response.ok) {
        await errorHandler(response);
    }

    return response.json();
}

export async function logout() {
    const response = await fetch(`${authUrl}/logout`, {
        method: "POST",
        credentials: "include"
    });

    await readResponse(response);
    accessToken = null;
}