<template>
    <transition name="modal_addNewVideo">
        <form>
            <div class="modal-mask" v-if="result === ''">
                <div class="modal-wrapper">
                    <div class="modal-container">
                        <div class="modal-body">
                            <slot name="body">
                                <div class="form-group">
                                    <div>
                                        <label for="video_name">Введите название видео</label>
                                        <input id="video_name" type="text" class="form-control w-100" placeholder="Название" v-model="name">
                                        <div class="error" v-if="invalidVideoName">
                                            <div v-if="!$v.name.required">Введите название видео</div>
                                            <div v-if="!$v.name.minLength">Название видео должно быть не меньше 5 символов</div>
                                            <div v-else-if="!$v.name.maxLength">Название видео должно быть не больше 30 символов</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div>
                                        <label for="video_description">Введите описание видео (необязательно)</label>
                                        <textarea id="video_description" type="text" class="form-control w-100" placeholder="Описание" v-model="description"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div>
                                        <label >Выберите загружаемый файл:</label>
                                        <input type="file" ref="video" accept="video/*" @change="loadVideo">
                                        <div class="error" v-if="invalidVideoFile">
                                            <div v-if="!$v.video.required">
                                            Выберите загружаемый файл
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div>
                                        <label>Выберите превью</label>
                                        <input type="file" ref="preview" accept="image/*" @change="loadPreview">
                                        <div class="error" v-if="invalidPreviewFile">
                                            <div v-if="!$v.preview.required">Выберите превью для видео</div>
                                        </div>
                                    </div>
                                </div>
                            </slot>
                        </div>

                        <div class="modal-footer">
                            <slot name="footer">
                                <button type="submit" class="modal-default-button" @click="addVideo" :disabled="sendData">
                                    Добавление видео
                                </button>
                                <button class="modal-default-button" @click="close">
                                    Отмена
                                </button>
                            </slot>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-mask" v-else>
                <div class="modal-wrapper">
                    <div class="modal-container">
                        <div class="modal-body">
                            {{result}}
                        </div>
                        <div class="modal-footer">
                            <slot name="footer">
                                <button class="modal-default-button" @click="close">
                                    Выход
                                </button>
                            </slot>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </transition>
</template>

<script>
    import {HTTP} from "./http";
    const { required, minLength, maxLength } = require('vuelidate/lib/validators');

    export default {
        name: 'Modal_AddNewVideo',

        data(){
            return{
                name: "",
                video: null,
                preview: null,
                description: "",
                result: "",
                sendData: false
            }
        },

        computed: {
            invalidVideoName : function(){
                return this.$v.name.$anyError;
            },
            invalidVideoFile : function(){
                return this.$v.video.$anyError;
            },
            invalidPreviewFile : function(){
                return this.$v.preview.$anyError;
            },
        },

        validations: {
            name: {
                required,
                minLength: minLength(5),
                maxLength: maxLength(30)
            },
            video: {
                required
            },
            preview: {
                required
            }
        },

        methods: {
            close : function() {
                this.description = this.result = this.name = "";
                this.preview = this.video = null;
                this.sendData = false;
                this.$emit('close');
            },

            addVideo : function () {
                this.$v.$touch();
                if(!this.$v.$invalid) {
                    this.sendData = true;
                    let data = new FormData();
                    data.append('name', this.name);
                    data.append('video', this.video);
                    data.append('preview', this.preview);
                    if(this.description !== '')
                        data.append('description', this.description);

                    HTTP.post('/videos/create', data, {
                            'Content-Type': 'multipart/form-data'
                        }
                    )
                    .then(response => {
                        if(response.data.status === 'success')
                            this.result = 'Видео успешно добавлено!!!';
                        else
                            this.result = 'Не удалось добавить видео!!!';
                        this.sendData = false;
                    })
                    .catch(() =>{
                        this.result = 'Нет отклика от сервера';
                        this.sendData = false;
                    });
                }
            },

            loadVideo : function(e){
                this.video= e.target.files[0];
            },

            loadPreview : function(e){
                this.preview = e.target.files[0];
            }
        },
    };
</script>
