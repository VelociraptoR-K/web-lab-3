import { register, login } from "./auth.js";
import { showError } from "./errors.js";

const form = document.getElementById("register-form");
const password = document.getElementById("password");
const passwordConfirm = document.getElementById("password-confirm");
const submitButton = form.querySelector('button[type="submit"]');
const errorContainer = document.querySelector(".error-container");

function validatePasswordConfirmation() {
    passwordConfirm.setCustomValidity(
        password.value === passwordConfirm.value
            ? ""
            : "Пароли не совпадают"
    );
}

password.addEventListener("input", validatePasswordConfirmation);
passwordConfirm.addEventListener("input", validatePasswordConfirmation);

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    errorContainer.hidden = true;

    validatePasswordConfirmation();

    if (!form.reportValidity()) {
        return;
    }

    const formData = new FormData(form);
    const username = formData.get("username").trim();
    const email = formData.get("email").trim();
    const rawPassword = formData.get("password");

    submitButton.disabled = true;

    try {
        await register(username, email, rawPassword);
    } catch (error) {
        showError(error);
        submitButton.disabled = false;
        return;
    }

    try {
        await login(username, rawPassword);
        window.location.replace("index.html");
    } catch (error) {
        showError(new Error(
            `Аккаунт создан. Войдите через страницу входа.\n${error.message}`
        ));
        submitButton.disabled = false;
    }
});