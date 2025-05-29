import { dev } from "$app/environment";
import { error } from "@sveltejs/kit";
import { codeToTokens } from 'shiki'

export async function load({ params }: { params: { slug: string } }) {
  let path = params.slug;

  let backendHost = "backend:8080";
  if (dev) {
    backendHost = "http://localhost:8080";
  }

  let response = await fetch(
    `${backendHost}/get/${path}/metadata`
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
  let contentPromise = fetch(`${backendHost}/get/${path}/content`).then(
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