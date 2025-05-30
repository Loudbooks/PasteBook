import { dev } from "$app/environment";
import { error } from "@sveltejs/kit";
import { bundledLanguages, codeToTokens } from 'shiki'
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ request, params }) => {
  let path = params.slug;

  let backendHost = "http://backend:8080";
  if (dev) {
    backendHost = "http://localhost:8080";
  }

  let ip = request.headers.get("CF-Connecting-IP") || request.headers.get("X-Real-IP") || request.headers.get("X-Forwarded-For") || request.headers.get("X-Forwarded") || "Unknown";

  let metadata = await fetch(
    `${backendHost}/get/${path}/metadata`, {
    headers: {
      "X-Real-IP": ip,
    }
  }
  );

  if (metadata.status === 404) {
    error(404, {
      code: 404,
      message: "Metadata Not Found",
    } satisfies App.Error);
  }
  if (metadata.status === 403) {
    error(403, {
      code: 403,
      message: "Forbidden",
    } satisfies App.Error);
  }
  if (metadata.status === 500) {
    error(500, {
      code: 500,
      message: "Server Error",
    } satisfies App.Error);
  }
  if (metadata.status === 429) {
    error(429, {
      code: 429,
      message: "Rate Limited",
    } satisfies App.Error);
  }

  let metadataPromise = metadata.json();

  let contentFetch = fetch(
    `${backendHost}/get/${path}/content`,
    {
      headers: {
        "X-Real-IP": ip,
      }
    }
  ).then((response) => {
    return response.text() as Promise<any>
  });

  let highlighterPromise = Promise.all([metadataPromise, contentFetch]).then(async ([metadata, content]) => {
    if (!metadata.language) {
      return null;
    }

    if (Object.keys(bundledLanguages).find(lang => lang == metadata.language.toLowerCase()) == undefined) {
      metadata.language = null;
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
    content: contentFetch,
    highlightedContent: highlighterPromise,
  };
}