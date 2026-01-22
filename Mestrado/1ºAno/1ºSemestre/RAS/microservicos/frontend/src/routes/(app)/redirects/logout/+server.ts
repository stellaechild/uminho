import { redirect } from '@sveltejs/kit';

export const GET = async ({ cookies }) => {
    cookies.delete('AuthToken', {path: '/'});
    let token = cookies.get('AuthToken');
    console.log(token);
	throw redirect(303, '/');
};