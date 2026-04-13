import { api } from "./api";

/*
export const getUserById = async (id) => {
  const res = await api.get(`/api/users/${id}`);
  return res.data;
};

export const getMe = async () => {
  const res = await api.get("/api/users/me");
  return res.data;
};

export const updateProfile = async (id, data) => {
  const res = await api.put(`/api/users/${id}`, data);
  return res.data;
};
*/

const mockUsers = [
  {
    id: 1,
    login: "chefanna",
    email: "anna@email.com",
    country: "Hungary",
    role: "user",
    allergies: ["nuts"],
  },
  {
    id: 2,
    login: "john",
    email: "john@email.com",
    country: "Germany",
    role: "admin",
    allergies: [],
  },
];

export const getUserById = async (id) => {
  return mockUsers.find((u) => u.id === Number(id));
};

export const getMe = async () => {
  return mockUsers[0];
};

export const updateProfile = async (id, data) => {
  const user = mockUsers.find((u) => u.id === Number(id));
  if (!user) return null;

  Object.assign(user, data);
  return user;
};