# Generated by Django 3.1.6 on 2021-02-23 01:56

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hospitals', '0006_auto_20210222_0310'),
    ]

    operations = [
        migrations.AddField(
            model_name='hospital',
            name='county',
            field=models.CharField(default='N/A', max_length=100),
        ),
    ]