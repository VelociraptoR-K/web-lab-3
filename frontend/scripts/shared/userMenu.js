import { logout, getCurrentUser } from "../api/auth.js";
import { showError } from "./errors.js";

const menu = document.getElementById("user-menu");
const username = document.getElementById("menu-username");
const logoutButton = document.getElementById("logout-button");



logoutButton.addEventListener("click", async () => {
    logoutButton.disabled = true;

    try {
        await logout();
        window.location.replace("login.html");
    } catch (error) {
        showError(error);
    } finally {
        logoutButton.disabled = false;
    }
});

try {
    const user = await getCurrentUser();

    document.querySelectorAll("[data-admin-only]").forEach(element => {
        element.hidden = user.role !== "ADMIN";
    });

    username.textContent = user.username;
    menu.hidden = false;
} catch (error) {
    showError(error);
}