<template>
    <div>
        <h1 class="m-auto text-center">Это страница пользователя {{username}}</h1>
        <div class="album py-5" style="display: flex; flex-direction: column; align-items: center;">
            <nav>
                <div class="nav nav-tabs" id="nav-tab" role="tablist">
                    <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#favorits" role="tab" aria-controls="favorits" aria-selected="false">Понравившееся</a>
                    <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#loads" role="tab" aria-controls="loads" aria-selected="false">Загруженные</a>
                    <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab" href="#playlists" role="tab" aria-controls="playlists" aria-selected="false">Плейлисты</a>
                </div>
            </nav>
            <div class="tab-content" id="nav-tabContent">
                <div class="tab-pane fade show active" id="favorits" role="tabpanel" aria-labelledby="favorits-tab">
                    <div class="album py-5">
                        <div class="container">
                            <div class="row">
                                <div v-for="video in favorite_video" :key="video.id" class="col-md-3">
                                    <router-link :to="{name:'video', params:{id:video.id} }">
                                        <div class="card mb-4 shadow-sm">
                                            <img :src="video.preview" alt = "Невозможно" width="100%" height="100%">
                                            <div class = "card-body">
                                                <div class="card-text">
                                                    {{video.name}}
                                                </div>
                                            </div>
                                        </div>
                                    </router-link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="loads" role="tabpanel" aria-labelledby="loads-tab">
                    <div class="album py-5">
                        <div class="container">
                            <div class="row">
                                <div v-for="video in loaded_video" :key="video.id" class="col-md-3">
                                    <router-link :to="{name:'video', params:{id:video.id} }">
                                        <div class="card mb-4 shadow-sm">
                                            <img :src="video.preview" alt = "Невозможно" width="100%" height="100%">
                                            <div class = "card-body">
                                                <div class="card-text">
                                                    {{video.name}}
                                                </div>
                                            </div>
                                        </div>
                                    </router-link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="playlists" role="tabpanel" aria-labelledby="playlists-tab"></div>
            </div>


        </div>
    </div>
</template>

<script>
    import {HTTP} from "../components/http";

    export default {
        name: "UserPage",

        data() {
            return {
                loaded_video: [],
                favorite_video: [],
                playlists: [],
            }
        },

        created() {
            HTTP.get("users/loaded_video?username=" + this.username)
                .then( response => this.loaded_video = response.data )
            HTTP.get("users/favorite_video?username=" + this.username)
                .then( response => this.favorite_video = response.data )
        },

        computed: {
            username : function () {
                return this.$root.$route.params.username;
            }
        },
    }
</script>