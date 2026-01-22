// See https://kit.svelte.dev/docs/types#app

import type { UserTypes } from "$lib/types";


// for information about these interfaces
declare global {
	namespace App {
	interface Error {
		title?: string;
		message?: string;
		redirect?: string;
	}
	interface Locals {
		user: User;
	};
	}
	// interface PageData {}
	// interface Platform {}
}
export { };
