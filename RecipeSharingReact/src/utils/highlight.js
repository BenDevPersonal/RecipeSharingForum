export function highlightParts(text, query) {
  if (!query) return [{ text, match: false }];

  const safeQuery = query.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  const parts = String(text).split(new RegExp(`(${safeQuery})`, "gi"));

  return parts.map((part) => ({
    text: part,
    match: part.toLowerCase() === query.toLowerCase(),
  }));
}