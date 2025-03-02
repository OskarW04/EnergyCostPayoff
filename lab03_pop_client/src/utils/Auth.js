export const isLoggedIn = () => !!sessionStorage.getItem("token");

export const getRole = () => {
    const token = sessionStorage.getItem("token");
    if (!token) return null;
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.role;
};


export const logout = () => {
    sessionStorage.removeItem("token");
    window.location.href = "/";
};