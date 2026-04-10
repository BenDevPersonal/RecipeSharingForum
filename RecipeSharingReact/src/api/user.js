{/*import { api } from "./api";

export const getProfile = async () => {
  const res = await api.get("/api/users/me");
  return res.data;
};*/}


{/* Temporary until backend security and auth is set up*/}
export const getProfile = async () => {
  return {
    login: "demoUser",
    email: "demo@email.com",
    country: { name: "Hungary" },
    role: { name: "user" },
  };
};