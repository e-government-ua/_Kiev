<?php

use app\components\ActiveField;
use yii\bootstrap\ActiveForm;

$this->title = 'Портал -= Электронное правительство =-';
?>
<div class="page-header">
  <h2>Будь ласка заповніть форму <?php echo $definition["processDefinitionId"]; ?></h2>
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
<!--                    <div class="form-group field-processdefinitionform-doctype">
                        <label class="control-label" for="processdefinitionform-doctype">Заявник</label>
                        <h4>Заявник <?= Yii::$app->user->loadUser()->username?></h4>
                    </div>-->

                    <?php foreach($model->attributes as $propertyName => $item): ?>
                        <?php
                            $field = $model->getField($propertyName);
                            $options = [];
                            if($field['type'] == app\models\ProcessDefinitionForm::TYPE_DATE) {
                                $options['inputTemplate'] = '<div class="row"><div class="col-lg-4">{input}</div></div>';
                            }
                            $formField = $form->field($model, $propertyName, $options);

                        ?>
                        <?= $model->getEditField($propertyName, $formField)?>
                    <?php endforeach; ?>

                    <button class="btn btn-lg btn-primary" type="submit">Подати заявку</button>
                </div>
            <?php ActiveForm::end(); ?>
        </div>
    </div>
</div>