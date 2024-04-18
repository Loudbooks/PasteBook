import {type Writable, writable} from "svelte/store";
import type {Issue} from "$lib/issue";

export const writableContent = writable("");
export const writableTitle = writable("");

export const severes: Writable<Array<Issue>> = writable([]);
export const warnings: Writable<Array<Issue>> = writable([]);

export const pasteName = writable("");
export const pasteURL = writable("");