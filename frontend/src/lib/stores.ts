import { type Writable, writable } from "svelte/store";
import type { Issue } from "$lib/issue";

export const loadProgress = writable(false);

export const writableContent = writable("");
export const writableTitle = writable("");

export const severes: Writable<Array<Issue>> = writable([]);
export const warnings: Writable<Array<Issue>> = writable([]);
export const validScan: Writable<Boolean> = writable(false);

export const pasteName = writable("");
export const pasteURL = writable("");

export const wrap = writable(false);
export const expire = writable(86400000);

export const title = writable("");
export const description = writable("");
export const disableNew = writable(false);
