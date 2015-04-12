<?php

use yii\helpers\Html;

?>

<div class="row">
    <div class="col-lg-12">
        <center>
            <h3>Шановний <?= Yii::$app->user->loadUser()->username?>!</h3>
            Ваше звернення №<?= $data['id']?> успішно зареєстровано<br>
            Результаты будуть спрямовані на Ваш e-mail <b><?= $data['email']?></b><br>
            <br>
            <?= Html::a('Повернутись до переліку послуг', Yii::$app->homeUrl)?>
        </center>
    </div>

</div>