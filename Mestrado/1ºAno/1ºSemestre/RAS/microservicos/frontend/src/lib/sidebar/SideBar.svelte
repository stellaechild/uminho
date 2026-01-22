<script lang="ts">
	import { UserTypes } from '$lib/types';
	import SideBarBox from './SideBarBox.svelte';
	import SidebarComponent from './SidebarComponent.svelte';
	export let type: UserTypes = UserTypes.NotAuthenticated;
</script>

<SideBarBox>
	<div class="w-full h-full flex flex-col">
		<a href="/" class="btn btn-outline h-24 mx-4 my-2">
			<h1 class="text-2xl font-bold text-center my-8">Probum</h1>
		</a>
		<div class="divider divider-neutral mx-8"></div>
		{#if type !== 0}
			<SidebarComponent link="/perfil">
				<span>Perfil</span>
			</SidebarComponent>
			{#if type === UserTypes.ESTUDANTE}
				<SidebarComponent link="/provas">
					<span>Provas</span>
				</SidebarComponent>
                <SidebarComponent link="/notificacoes">
					<span>Notificacoes</span>
				</SidebarComponent>
            {:else if type === UserTypes.DOCENTE}
                <SidebarComponent link="/provas">
                    <span>Provas</span>
                </SidebarComponent>
                <SidebarComponent link="/notificacoes">
                    <span>Notificacoes</span>
                </SidebarComponent>
                <SidebarComponent link="/registar/aluno">
                    <span>Registar alunos</span>
                </SidebarComponent>
            {:else if type === UserTypes.TECNICO}
                <SidebarComponent link="/notificacoes">
                    <span>Notificacoes</span>
                </SidebarComponent>
                <SidebarComponent link="/registar/aluno">
                    <span>Registar aluno</span>
                </SidebarComponent>
                <SidebarComponent link="/registar/docente">
                    <span>Registar docente</span>
                </SidebarComponent>
                <SidebarComponent link="/salas">
                    <span>Gerir Salas</span>
                </SidebarComponent>
			{/if}
            <div class="grow"></div>
			<div class="w-full self-end">
				<SidebarComponent link="/redirects/logout">
					<span>Logout</span>
				</SidebarComponent>
			</div>
		{:else}
			<div class="grow"></div>
			<div class="w-full self-end">
				<SidebarComponent link="/login">
					<span>Login</span>
				</SidebarComponent>
			</div>
		{/if}
	</div>
</SideBarBox>
