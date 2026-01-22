import { verifyJWT } from '$lib/jwt/token';
import { UserTypes } from '$lib/types';
import type { Handle } from '@sveltejs/kit';
export const handle: Handle = async ({ event, resolve }) => {
	const authCookie = event.cookies.get('AuthToken');
	event.locals.user = {
		id: "",
		email: "",
		type: UserTypes.NotAuthenticated,
	};
	if (authCookie) {
		try {
			const token = authCookie.split('Bearer ')[1];
			const user = await verifyJWT<{ id: string, sub: string }>(token);
			let  sub = user.sub.split(';');
			event.locals.user = {
				id: user.id,
				email: sub[0],
				type: Number(sub[1]),
			};
		} catch (err) {
			console.log("Error decoding JWT - ", err);
		}
	}
	const response = await resolve(event);
	return response;
};