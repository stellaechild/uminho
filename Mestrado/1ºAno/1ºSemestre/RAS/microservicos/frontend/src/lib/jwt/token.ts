import { SignJWT, jwtVerify, type JWTPayload } from 'jose';

export const verifyJWT = async <T>(token: string): Promise<T> => {
	try {
		return (await jwtVerify(token, new TextEncoder().encode("secret"))).payload as T;
	} catch (error) {
		console.log(error);
		throw new Error('Your token has expired.');
	}
};
