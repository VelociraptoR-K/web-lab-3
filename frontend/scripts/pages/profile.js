import { logout, getCurrentUser } from "../api/auth.js";
import { showError } from "../shared/errors.js";

const profileCard = document.getElementById("profile-card");
const username = document.getElementById("profile-username");
const email = document.getElementById("profile-email");
const role = document.getElementById("profile-role");
const logoutButton = document.getElementById("delete-button");
const errorContainer = document.querySelector(".error-container");

async function loadProfile() {
    try {
        const user = await getCurrentUser();

        username.textContent = user.username;
        email.textContent = user.email;
        role.textContent = {
            USER: "Пользователь",
            ADMIN: "Администратор"
        }[user.role] ?? user.role;

        profileCard.hidden = false;
    } catch (error) {
        showError(error);
    }
}

logoutButton.addEventListener("click", async () => {
    errorContainer.hidden = true;
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

await loadProfile();