import { useState } from "react";

export function AdminDashboard() {
  const [tab, setTab] = useState("users");

  return (
    <div className="max-w-6xl mx-auto p-8">
      <h1 className="text-4xl font-bold mb-6">🛠 Admin Dashboard</h1>

      <div className="flex gap-4 mb-6">
        <Tab name="users" tab={tab} setTab={setTab} />
        <Tab name="posts" tab={tab} setTab={setTab} />
        <Tab name="categories" tab={tab} setTab={setTab} />
        <Tab name="allergies" tab={tab} setTab={setTab} />
      </div>

      {tab === "users" && <UsersTab />}
      {tab === "posts" && <PostsTab />}
      {tab === "categories" && <CategoriesTab />}
      {tab === "allergies" && <AllergiesTab />}
    </div>
  );
}

function Tab({ name, tab, setTab }) {
  return (
    <button
      onClick={() => setTab(name)}
      className={`px-4 py-2 rounded ${
        tab === name ? "bg-accent text-white" : "bg-gray-200"
      }`}
    >
      {name}
    </button>
  );
}