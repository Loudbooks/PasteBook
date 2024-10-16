import { error } from "@sveltejs/kit";
import { pasteURL } from "$lib/stores";

export async function load({ params, cookies }) {
  let path = params.slug;

  let response = await fetch(
    "http://localhost:25658/api/get/" + path + "/metadata",
  );

  if (response.status === 404) {
    error(404, {
      message: "Content Not Found",
    });
  }

  if (response.status === 403) {
    error(403, {
      message: "Forbidden",
    });
  }

  if (response.status === 500) {
    error(500, {
      message: "Server Error",
    });
  }

  if (response.status === 429) {
    error(429, {
      message: "Rate Limited",
    });
  }

  pasteURL.set("https://pastebook.dev/p/" + path);

  let metadataPromise = response.json();

  return {
    metadata: metadataPromise,
    url: "https://pastebook.dev/api/get/" + path + "/content",
    inspect: cookies.get("inspect") === "true",
  };
}
