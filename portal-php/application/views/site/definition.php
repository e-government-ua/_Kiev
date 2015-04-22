<?php

use app\components\ActiveField;
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
                            if($field['type'] == app\models\ProcessDefinitionForm::TYPE_DATE) {
                                $options['inputTemplate'] = '<div class="row"><div class="col-lg-4">{input}</div></div>';
                            }
                            $formField = $form->field($model, $propertyName, $options);

                        ?>
                        <?= $model->getEditField($propertyName, $formField); ?>
                    <?php endforeach; ?>

                    <button class="btn btn-lg btn-primary" type="submit">Подати заявку</button>
                </div>
            <?php ActiveForm::end(); ?>
        </div>
    </div>
</div>