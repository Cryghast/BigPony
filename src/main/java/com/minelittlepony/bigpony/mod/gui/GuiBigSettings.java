package com.minelittlepony.bigpony.mod.gui;

import com.minelittlepony.bigpony.mod.BigPony;
import com.minelittlepony.bigpony.mod.CameraPresets;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider.FormatHelper;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import javax.annotation.Nonnull;

public class GuiBigSettings extends GuiScreen implements GuiResponder, FormatHelper {

    private BigPony bigPony;

    private ResettableSlider xSize, ySize, zSize, height, distance;

    private CameraPresetButton[] cameraPresets, scalePresets, combinedPresets;

    public GuiBigSettings(BigPony bigPony) {
        this.bigPony = bigPony;
    }

    @Override
    public void initGui() {
        // sliders
        xSize = new ResettableSlider(this.buttonList, this, 1, 5, 40, "X Scale", .1F, 2F, bigPony.getxScale(), this);
        ySize = new ResettableSlider(this.buttonList, this, 2, 5, 60, "Y Scale", .1F, 2F, bigPony.getyScale(), this);
        zSize = new ResettableSlider(this.buttonList, this, 3, 5, 80, "Z Scale", .1F, 2F, bigPony.getzScale(), this);

        height = new ResettableSlider(this.buttonList, this, 4, 5, 100, "Eye Height", .1F, 2F, bigPony.getHeight(), this);
        distance = new ResettableSlider(this.buttonList, this, 5, 5, 120, "Camera Distance", .1F, 2F, bigPony.getDistance(), this);

        CameraPresets[] values = CameraPresets.values();
        // presets

        cameraPresets = new CameraPresetButton[values.length];
        scalePresets = new CameraPresetButton[values.length];
        combinedPresets = new CameraPresetButton[values.length];

        for (int i = 0; i < cameraPresets.length; i++) {
            buttonList.add(cameraPresets[i] = new CameraPresetButton(this, values[i], 200, 40, true, false));
            buttonList.add(scalePresets[i] = new CameraPresetButton(this, values[i], 300, 40, false, true));
            buttonList.add(combinedPresets[i] = new CameraPresetButton(this, values[i], 280, 40, true, true));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, "BigPony settings", width / 2, 10, -1);
        this.drawCenteredString(this.fontRenderer, "Camera Presets", 240, 25, -1);
        this.drawCenteredString(this.fontRenderer, "Body Presets", 330, 25, -1);
    }

    @Override
    public void updateScreen() {
        CameraPresets[] values = CameraPresets.values();
        for (int i = 0; i < cameraPresets.length; i++) {
            cameraPresets[i].enabled = !values[i].isEqual(height.getSliderValue(), distance.getSliderValue());
            scalePresets[i].enabled = !values[i].isEqual(xSize.getSliderValue(), ySize.getSliderValue(), zSize.getSliderValue());
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof IPerformable) {
            ((IPerformable)button).performAction();
        }
    }

    public void applyPreset(CameraPresets preset, boolean camera, boolean body) {
        float h = preset.getHeight();
        if (body) {
            xSize.setSliderValue(h, true);
            ySize.setSliderValue(h, true);
            zSize.setSliderValue(h, true);
        }
        if (camera) {
            height.setSliderValue(h, true);
            distance.setSliderValue(preset.getDistance(), true);
        }
    }

    @Override
    public void setEntryValue(int id, boolean value) {

    }

    @Override
    public void setEntryValue(int id, float value) {
        float x = bigPony.getxScale();
        float y = bigPony.getyScale();
        float z = bigPony.getzScale();
        switch (id) {
            case 1:
                bigPony.setScale(value, y, z);
                break;
            case 2:
                bigPony.setScale(x, value, z);
                break;
            case 3:
                bigPony.setScale(x, y, value);
                break;
            case 4:
                bigPony.setHeight(value);
                break;
            case 5:
                bigPony.setDistance(value);
                break;
        }
    }

    @Override
    public void setEntryValue(int id, @Nonnull String value) {

    }

    @Override
    @Nonnull
    public String getText(int id, @Nonnull String name, float value) {
        return String.format("%s: %d%%", name, MathHelper.ceil(value * 100));
    }
}
