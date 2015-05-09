<?php

use app\components\ActiveField;
use app\models\ProcessDefinitionForm;
use dosamigos\fileupload\FileUpload;
use yii\bootstrap\ActiveForm;

$this->title = 'Портал -= Электронное правительство =-';
?>
<div class="page-header">
<h1><?php echo $model->getFormName(); ?></h1>
</div>

<div class=row">
    <div class="col-lg-10">
        <div class="box box-primary">
            <?php $form = ActiveForm::begin([
                'fieldConfig' => [
                    'class' => ActiveField::className()
                ]
            ]); ?>
                <div class="box-body">
                    <?= $form->errorSummary($model); ?>

                    <?php foreach($model->attributes as $propertyName => $item): ?>
                        <?php
                            $field = $model->getField($propertyName);
                            $options = [];
                            if($field['type'] == ProcessDefinitionForm::TYPE_DATE) {
                                $options['inputTemplate'] = '<div class="row"><div class="col-lg-4">{input}</div></div>';
                            }
                            $formField = $form->field($model, $propertyName, $options);

                        ?>
                        <?= $model->getEditField($propertyName, $formField); ?>

                        <!--?php if($propertyName == 'attachedId'): ?-->
                        <?php if($field['type'] == ProcessDefinitionForm::TYPE_FILE): ?>
                            <div class="fileinput-button">
                                <button type="button" class="btn btn-success js-upload-btn" data-loading-text="Завантаження..." class="btn btn-primary" autocomplete="off">
                                    <span class="glyphicon glyphicon-file" aria-hidden="true"></span> Завантажити файл
                                </button>
                                <?=   FileUpload::widget([
                                    'name' => 'file',
                                    'url' => ['/site/upload'],
                                    'options' => ['accept' => '*', 'class' => ''],
                                    'clientOptions' => [
                                        'maxFileSize' => 2000000,
                                        'autoUpload' => true
                                    ],
                                    // see: https://github.com/blueimp/jQuery-File-Upload/wiki/Options#processing-callback-options
                                    'clientEvents' => [
                                        'fileuploadsubmit' => 'function(e, data) {
                                            $(".js-upload-btn").button("loading");
                                        }',
                                        'fileuploaddone' => 'function(e, data) {
                                            $(".js-upload-btn").button("reset");
                                            $(".js-attached-id").val(data.result.id);
                                            $(".js-filename").html(data.result.name);
                                        }',
                                        'fileuploadfail' => 'function(e, data) {
                                            $(".js-upload-btn").button("reset");
                                            //$(".js-filename").html(data.textStatus);
                                            $(".js-filename").html("При завантаженні файлу виникла помилка");
                                        }',
                                    ],
                                ]);?>
                            </div>
                            <div class="filename js-filename"></div>
                            <br>
                        <?php endif; ?>
                    <?php endforeach; ?>

                    <button class="btn btn-lg btn-primary" type="submit">Подати заявку</button>
                </div>
            <?php ActiveForm::end(); ?>
        </div>
    </div>
</div>