import { error } from "@sveltejs/kit";
import { codeToTokens } from 'shiki'

export async function load({ params }) {
  let path = params.slug;

  let response = await fetch(
    "http://backend:8080/get/" + path + "/metadata",
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

  let metadataPromise = response.json();
  let contentPromise = fetch("http://backend:8080/get/" + path + "/content").then(
    (response) => {
      return response.text();
    }
  );

  let highlighterPromise = Promise.all([metadataPromise, contentPromise]).then(async ([metadata, content]) => {
    if (!metadata.language) {
      return null;
    }

    const tokenLines = await codeToTokens(content, {
      lang: metadata.language.toLowerCase(),
      theme: "ayu-dark",
    });

    return tokenLines.tokens;
  })

  return {
    metadata: metadataPromise,
    content: contentPromise,
    highlightedContent: highlighterPromise,
  };
}