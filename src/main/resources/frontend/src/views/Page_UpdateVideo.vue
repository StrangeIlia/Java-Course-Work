<template>
    <div class="album py-5">
        <div style="display: flex; flex-direction: row; justify-content: center;">Выберите видео для обновления: </div>
        <div class="container">
            <div class="row">
                <div v-for="video in videos" :key="video.id" class="col-md-3">
                    <div class="card mb-4 shadow-sm" @click="open_UpdateVideo(video)">
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

        <modal_ChangeVideo v-show="modal.updateVideo" v-bind:video="modal.updateVideoParams.video" @close="close_UpdateVideo" ref="updateVideo"/>

    </div>
</template>

<script>
    import { HTTP } from "../components/http";
    import modal_ChangeVideo from "../components/Modal_ChangeVideo";

    export default {
        components: {
            modal_ChangeVideo
        },

        name: 'Videos',
        data () {
            return{
                videos: [],
                modal: {
                    updateVideo: false,
                    updateVideoParams : {
                        video: null
                    }
                }
            }
        },

        methods: {
            open_UpdateVideo : function(video){
                this.modal.updateVideoParams.video = video;
                this.$refs.updateVideo.localCopy.id = video.id;
                this.$refs.updateVideo.localCopy.name = video.name;
                this.$refs.updateVideo.localCopy.description = video.description === null ? '' : video.description;
                this.modal.updateVideo = true;
            },

            close_UpdateVideo : function () {
              this.modal.updateVideo = false;
              this.modal.updateVideoParams.video = null;
            },
        },

        created() {
            HTTP.get('/users/loaded_video?username=')
                .then(response => {this.videos = response.data;});
        }
    }

</script>
