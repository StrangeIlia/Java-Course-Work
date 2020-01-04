<template>
    <div class="album py-5">
        <div class="container">
            <div class="row">
                <div v-for="playlist in playlists" :key="playlist.id" class="col-md-3">
                    <router-link :to="{name:'playlist', params:{id:playlist.id}}">
                        <div class="card mb-4 shadow-sm">
                            <img :src="video.preview" alt = "Невозможно">
                            <div class = "card-body">
                                <div class="card-text">
                                    {{playlist.name}}
                                </div>
                            </div>
                        </div>
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {HTTP} from "../components/http";

    export default {
        name: "Playlists",
        data(){
            return {
                playlists: []
            }
        },

        created() {
            let data = new FormData();
            data.append('user_login', this.$route.params.selected_user);
            HTTP.post('users/get_playlists', data).then(response => (this.playlists = response.playlists));
        }
    }
</script>
