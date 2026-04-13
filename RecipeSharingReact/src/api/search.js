import { api } from "./api";

/*
export const searchPosts = async (q, category, allergy) => {
  const res = await api.get("/api/posts/search", {
    params: { q, category, allergy },
  });
  return res.data;
};

export const searchUsers = async (q) => {
  const res = await api.get("/api/users/search", {
    params: { q },
  });
  return res.data;
};
*/

const mockPosts = [
  {
    id: 1,
    title: "Pizza Margherita",
    author: "chefanna",
    rating: 4.5,
    categories: ["italian", "pizza"],
    allergies: ["gluten"],
  },
  {
    id: 2,
    title: "Chicken Curry",
    author: "john",
    rating: 4.0,
    categories: ["indian"],
    allergies: ["nuts"],
  },
  {
    id: 3,
    title: "Chocolate Cake",
    author: "sara",
    rating: 4.8,
    categories: ["dessert"],
    allergies: ["eggs", "gluten"],
  },
];

const mockUsers = [
  {
    id: 1,
    login: "chefanna",
    email: "anna@email.com",
  },
  {
    id: 2,
    login: "john",
    email: "john@email.com",
  },
];

export const searchPosts = async (q, category, allergy) => {
  return mockPosts
    .filter((p) =>
      p.title.toLowerCase().includes((q || "").toLowerCase())
    )
    .filter((p) => {
      if (!category) return true;
      return p.categories.includes(category.toLowerCase());
    })
    .filter((p) => {
      if (!allergy) return true;
      return !p.allergies.includes(allergy.toLowerCase());
    })
    .sort((a, b) => b.rating - a.rating);
};

export const searchUsers = async (q) => {
  return mockUsers.filter((u) =>
    u.login.toLowerCase().includes((q || "").toLowerCase())
  );
};