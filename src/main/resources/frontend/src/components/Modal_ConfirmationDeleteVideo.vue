<template>
    <transition name="modal_confirmationDeleteVideo">
        <form>
            <div class="modal-mask" v-if="resultOperation === ''">
                <div class="modal-wrapper">
                    <div class="modal-container">
                        <div class="modal-body">
                            <slot name="body">
                                <div>
                                    Вы уверены, что хотите удалить это видео?
                                </div>
                            </slot>
                        </div>

                        <div class="modal-footer">
                            <slot name="footer">
                                <button type="submit" class="modal-default-button" @click="deleteVideo">
                                    Да
                                </button>
                                <button class="modal-default-button" @click="close">
                                    Нет
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
                            {{resultOperation}}
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

    export default {
        name: "Modal_ConfirmationDeleteVideo",
        props: ['video'],

        data() {
            return {
                resultOperation: ''
            }
        },

        methods: {
            close : function() {
                this.resultOperation = '';
                this.$emit('close');
            },

            deleteVideo : function () {
                HTTP.delete('videos/delete?id=' + this.video.id )
                .then( response => {
                    let tmp = response; // Чтобы vue не ругался, что я не использую переменные ХД
                    response = tmp;

                    this.resultOperation = 'Удаление успешно';
                    let parrent = this.$parent;
                    let removeIndex = parrent.videos.findIndex((item, index, array ) => {
                        index = array;
                        return item.id === this.video.id
                    });
                    //this.resultOperation = removeIndex;
                    parrent.videos.splice(removeIndex, 1);
                }).catch(() => {
                    this.resultOperation = 'Возникла ошибка при удалении видео, попробуйте позже';
                    this.$parent.modals.deleteVideoResult.result = false;
                });
            }
        }
    }
</script>
