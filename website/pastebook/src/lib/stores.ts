import {type Writable, writable} from "svelte/store";
import type {Issue} from "$lib/issue";

export const loadProgress = writable(0);

export const writableContent = writable("");
export const writableTitle = writable("");

export const severes: Writable<Array<Issue>> = writable([]);
export const warnings: Writable<Array<Issue>> = writable([]);
export const validScan: Writable<Boolean> = writable(false);

export const pasteName = writable("");
export const pasteURL = writable("");

export const wrap = writable(false);
export const unlisted = writable(false);