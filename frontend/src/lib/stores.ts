import { writable } from "svelte/store";

export const wrap = writable(false);
export const time = writable("24h");
export const burn = writable(false);

export const writableContent = writable("");
export const writableTitle = writable("");

export const disableNew = writable(false);
export const title = writable("");