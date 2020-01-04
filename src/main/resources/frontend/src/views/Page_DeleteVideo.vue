<template>
    <div class="album py-5">
        <div style="display: flex; flex-direction: row; justify-content: center;">Выберите видео для удаления: </div>
        <div class="container">
            <div class="row">
                <div v-for="video in videos" :key="video.id" class="col-md-3">
                    <div class="card mb-4 shadow-sm" @click="open_DeleteVideo(video)">
                        <img :src="video.preview" alt = "Невозможно" width="100%" height="100%">
                        <div class = "card-body">
                            <div class="card-text">
                                {{video.name}}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <modal_confirmationDeleteVideo v-show="modal.deleteVideo" v-bind:video="modal.deleteVideoParams.video" @close="close_DeleteVideo"/>

    </div>
</template>

<script>
    import { HTTP } from "../components/http";
    import modal_confirmationDeleteVideo from "../components/Modal_ConfirmationDeleteVideo";

    export default {
        components: {
            modal_confirmationDeleteVideo
        },

        name: 'Videos',
        data () {
            return{
                videos: [],
                modal: {
                    deleteVideo: false,
                    deleteVideoParams : {
                        video: null
                    }
                }
            }
        },

        methods: {
            open_DeleteVideo : function(video){
                this.modal.deleteVideoParams.video = video;
                this.modal.deleteVideo = true;
            },

            close_DeleteVideo : function () {
              this.modal.deleteVideo = false;
              this.modal.deleteVideoParams.video = null;
            }
        },

        created() {
            HTTP.get('/users/loaded_video?username=')
                .then(response => {this.videos = response.data;});
        }
    }

</script>
