<template>
    <transition name="modal_changeNewVideo">
        <form>
            <div class="modal-mask" v-if="result === ''">
                <div class="modal-wrapper">
                    <div class="modal-container">
                        <div class="modal-body">
                            <slot name="body">
                                <div class="form-group">
                                    <div>
                                        <label for="video_name">Введите название видео</label>
                                        <input id="video_name" type="text" class="form-control w-100" placeholder="Название" v-model="localCopy.name">
                                        <div class="error" v-if="invalidVideoName">
                                            <div v-if="!$v.video.localCopy.name.required">Введите название видео</div>
                                            <div v-if="!$v.video.localCopy.name.minLength">Название видео должно быть не меньше 5 символов</div>
                                            <div v-else-if="!$v.video.localCopy.name.maxLength">Название видео должно быть не больше 30 символов</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div>
                                        <label for="video_description">Введите описание видео (необязательно)</label>
                                        <textarea id="video_description" type="text" class="form-control w-100" placeholder="Описание" v-model="localCopy.description"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div>
                                        <label >Выберите загружаемый файл (необязательно):</label>
                                        <input type="file" ref="video" accept="video/*" @change="loadVideo">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div>
                                        <label>Выберите превью (необязательно):</label>
                                        <input type="file" ref="preview" accept="image/*" @change="loadPreview">
                                    </div>
                                </div>
                            </slot>
                        </div>

                        <div class="modal-footer">
                            <slot name="footer">
                                <button type="submit" class="modal-default-button" @click="updateVideo">
                                    Обновить видео
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
        name: 'Modal_ChangeVideo',
        props: ['video'],

        data() {
            return {
                localCopy: {
                    id: 0,
                    name: "",
                    description: "",
                    video: null,
                    preview: null,
                },
                result: '',
            }
        },

        computed: {
            invalidVideoName : function(){
                return this.$v.localCopy.name.$anyError;
            }
        },

        validations: {
            localCopy: {
                name: {
                    required,
                    minLength: minLength(5),
                    maxLength: maxLength(30)
                }
            }
        },

        methods: {
            close : function() {
                this.result = '';
                this.$emit('close');
            },

            updateVideo : function () {
                this.$v.$touch();
                if(!this.$v.$invalid) {
                    let data = new FormData();

                    if(this.localCopy.name !== this.video.name)
                        data.append('name', this.localCopy.name);

                    if(this.localCopy.description !== this.video.description)
                        data.append('description', this.localCopy.description);

                    if(this.localCopy.video !== null)
                        data.append('video', this.localCopy.video);

                    this.localCopy.video = null;

                    if(this.localCopy.preview !== null)
                        data.append('preview', this.localCopy.preview);

                    this.localCopy.preview = null;

                    let countValue = 1;
                    for (var key of data.keys()){
                        let tmp = key;
                        key = tmp;
                        let tmp2 = tmp;
                        tmp = tmp2;
                        ++countValue;
                    }

                    if(countValue === 0) {
                        this.result = 'Нет полей для изменения';
                        return;
                    }

                    HTTP.patch('/videos/update?id=' + this.video.id, data, {
                            'Content-Type': 'multipart/form-data',
                        }
                    )
                    .then(response => {
                        if(response.data.status === 'success'){
                            this.result = 'Обновление успешно';
                            // Так страшно, потому что мне надо записать содержимое, а не затереть ссылку
                            this.video.name = response.data.video.name;
                            this.video.path = response.data.video.path;
                            this.video.preview = response.data.video.preview;
                            this.video.description = response.data.video.description;
                        }
                        else {
                            this.result = response.data//'Не удалось обновить';
                        }
                    })
                    .catch(() =>{
                        this.result = 'Нет отклика от сервера';
                    });
                }
            },

            loadVideo : function(e){
                this.localCopy.video = e.target.files[0];
            },

            loadPreview : function(e){
                this.localCopy.preview = e.target.files[0];
            }
        },
    };
</script>
