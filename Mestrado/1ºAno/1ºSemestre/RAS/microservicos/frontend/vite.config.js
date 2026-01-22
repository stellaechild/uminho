import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vitest/config';

export default defineConfig({
	server: {
		host: "0.0.0.0",
		hmr: {
		  clientPort: 80,
		},
		port: 80, 
		watch: {
		  usePolling: true,
		},
		// https: true
	},
	plugins: [sveltekit()],
	test: {
		include: ['src/**/*.{test,spec}.{js,ts}']
	}
});
