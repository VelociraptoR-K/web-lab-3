import { getAccessToken, refresh } from "./auth.js";

let refreshPromise = null;

async function refreshOnce() {
    if (!refreshPromise) {
        refreshPromise = refresh().finally(() => {
            refreshPromise = null;
        });
    }

    return refreshPromise;
}

function send(url, options) {
    const headers = new Headers(options.headers);
    const token = getAccessToken();

    if (token) {
        headers.set("Authorization", `Bearer ${token}`);
    }

    return fetch(url, { ...options, headers });
}

export async function apiFetch(url, options = {}) {
    if (!getAccessToken()) {
        const restored = await refreshOnce();

        if (!restored) {
            window.location.replace("login.html");
            throw new Error("Необходимо войти в аккаунт");
        }
    }

    let response = await send(url, options);

    if (response.status === 401) {
        const restored = await refreshOnce();

        if (!restored) {
            window.location.replace("login.html");
            throw new Error("Сессия истекла. Войдите снова");
        }

        response = await send(url, options);
    }

    return response;
}