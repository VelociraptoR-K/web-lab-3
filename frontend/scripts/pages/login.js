import { login } from "../api/auth.js";
import { showError } from "../shared/errors.js";

const form = document.getElementById("login-form");
const submitButton = form.querySelector('button[type="submit"]');
const errorContainer = document.querySelector(".error-container");

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    errorContainer.hidden = true;

    const formData = new FormData(form);
    const username = formData.get("username").trim();
    const password = formData.get("password");

    submitButton.disabled = true;

    try {
        await login(username, password);
        window.location.replace("index.html");
    } catch (error) {
        showError(error);
    } finally {
        submitButton.disabled = false;
    }
});