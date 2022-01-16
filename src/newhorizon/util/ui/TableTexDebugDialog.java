package newhorizon.util.ui;

import arc.Core;
import arc.audio.Sound;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Action;
import arc.scene.actions.Actions;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Dialog;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Scaling;
import arc.util.Time;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Sounds;
import mindustry.gen.Tex;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Weather;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.storage.CoreBlock;
import newhorizon.NewHorizon;
import newhorizon.content.NHSounds;
import newhorizon.expand.block.defence.HyperSpaceWarper;
import newhorizon.expand.vars.TileSortMap;
import newhorizon.util.feature.InternalTools;
import newhorizon.util.feature.ScreenInterferencer;
import newhorizon.util.feature.WarpUnit;
import newhorizon.util.feature.cutscene.CutsceneScript;
import newhorizon.util.feature.cutscene.UIActions;
import newhorizon.util.func.NHInterp;
import newhorizon.util.func.NHPixmap;
import newhorizon.util.func.NHSetting;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import static mindustry.Vars.*;
import static newhorizon.util.ui.TableFunc.LEN;
import static newhorizon.util.ui.TableFunc.OFFSET;

public class TableTexDebugDialog extends BaseDialog{
	private BaseDialog
		buttonImage,
		buttonText,
		iconDialog,
		tableDialog;
	
	public TableTexDebugDialog(String title){
		this(title, Core.scene.getStyle(Dialog.DialogStyle.class));
		
		cont.defaults().size(LEN * 3, LEN).pad(OFFSET / 2).style(Styles.transt);
		
		cont.button("Icons", () -> {
			iconDialog = new BaseDialog("ICONS"){{
				Object obj = new Icon();
				Class<?> c = obj.getClass();
				
				Field[] fields = c.getFields();
				cont.pane(t -> {
					int index = 0;
					for(Field f : fields){
						try{
							if(f.getType().getSimpleName().equals("TextureRegionDrawable")){
								if(index % 6 == 0) t.row();
								t.table(inner -> {
									try{
										inner.image((Drawable)f.get(obj)).pad(OFFSET / 3);
									}catch(IllegalAccessException err){
										throw new IllegalArgumentException(err);
									}
									inner.add(f.getName());
								}).size(LEN * 3, LEN).pad(OFFSET / 3);
								index++;
							}
						}catch(IllegalArgumentException err){
							throw new IllegalArgumentException(err);
						}
					}
				}).grow();
			}};
			iconDialog.addCloseListener();
			iconDialog.show();
		});
		
		cont.button("TableTexes", () -> {
			tableDialog = new BaseDialog("ICONS"){{
				Class<?> c = Tex.class;
				
				Field[] fields = c.getFields();
				cont.pane(t -> {
					int index = 0;
					for(Field f : fields){
						try{
							if(Drawable.class.isAssignableFrom(f.getType())){
								if(index % 6 == 0) t.row();
								t.table(inner -> {
									try{
										inner.table((Drawable)f.get(null), de -> de.add(f.getName())).size(LEN * 3, LEN).pad(OFFSET / 3);
									}catch(IllegalAccessException err){
										throw new IllegalArgumentException(err);
									}
								}).size(LEN * 3, LEN).pad(OFFSET / 3);
								index++;
							}
						}catch(IllegalArgumentException err){
							throw new IllegalArgumentException(err);
						}
					}
				}).grow();
			}};
			tableDialog.addCloseListener();
			tableDialog.show();
		});
		
		cont.button("ButtonTexts", () -> {
			buttonText = new BaseDialog("ButtonTexts"){{
				Object obj = new Styles();
				Class<?> c = obj.getClass();
				
				Field[] fields = c.getFields();
				cont.pane(t -> {
					int index = 0;
					for(Field f : fields){
						try{
							if(f.getType().getSimpleName().equals("TextButtonStyle")){
								if(index % 6 == 0) t.row();
								t.table(inner -> {
									try{
										inner.button(f.getName(), (TextButton.TextButtonStyle)f.get(obj), () -> {}).size(LEN * 3, LEN).row();
										inner.button(f.getName(), (TextButton.TextButtonStyle)f.get(obj), () -> {}).size(LEN * 3, LEN).pad(OFFSET / 3).disabled(b -> true).row();
									}catch(IllegalAccessException err){
										throw new IllegalArgumentException(err);
									}
								}).grow().pad(OFFSET / 3);
								index++;
							}
						}catch(IllegalArgumentException err){
							throw new IllegalArgumentException(err);
						}
					}
				}).grow();
			}};
			buttonText.addCloseListener();
			buttonText.show();
		});
		
		cont.button("ButtonImages", () -> {
			buttonImage = new BaseDialog("ButtonImages"){{
				
				Object obj = new Styles();
				Class<?> c = obj.getClass();
				
				Field[] fields = c.getFields();
				cont.pane(t -> {
					int index = 0;
					for(Field f : fields){
						try{
							if(f.getType().getSimpleName().equals("ImageButtonStyle")){
								if(index % 6 == 0) t.row();
								t.table(inner -> {
									inner.table(de ->{
										try{
											de.button(Icon.none, (ImageButton.ImageButtonStyle)f.get(obj), () -> {}).size(LEN * 3, LEN).row();
										}catch(IllegalAccessException err){
											throw new IllegalArgumentException(err);
										}
										de.add(f.getName()).pad(OFFSET / 3);
									}).row();
									inner.table(de ->{
										try{
											de.button(Icon.none, (ImageButton.ImageButtonStyle)f.get(obj), () -> {}).disabled(b -> true).size(LEN * 3, LEN).row();
										}catch(IllegalAccessException err){
											throw new IllegalArgumentException(err);
										}
										de.add(f.getName()).pad(OFFSET / 3);
									}).row();
								}).grow().pad(OFFSET / 3);
								index++;
							}
						}catch(IllegalArgumentException err){
							throw new IllegalArgumentException(err);
						}
					}
				}).grow();
			}};
			buttonImage.addCloseListener();
			buttonImage.show();
		});
		
		cont.row();
		
		cont.button("Units", () -> {
			buttonImage = new BaseDialog("Units"){{
				cont.pane(table -> {
					AtomicInteger index = new AtomicInteger();
					
					content.units().each( (unit) -> {
						if(!unit.isHidden()){
							if(index.get() % 8 == 0) table.row();
							table.table(Tex.buttonEdge3, t -> {
								t.button(new TextureRegionDrawable(unit.fullIcon), Styles.cleari,LEN * 3,() -> {
									BaseDialog d = new BaseDialog("info"){{
										cont.image(unit.fullIcon);
									}};
									d.addCloseListener();
									d.show();
								}).grow().row();
								t.pane(in -> in.add(unit.localizedName).height(LEN).fillX()).height(LEN).growX();
							});
							index.getAndIncrement();
						}
					});
				}).fill();
			}};
			buttonImage.addCloseListener();
			buttonImage.show();
		});
		
		cont.button("UnlockALL", () -> {
			for(UnlockableContent content : content.items()){
				content.unlock();
			}
			for(UnlockableContent content : content.liquids()){
				content.unlock();
			}
			for(UnlockableContent content : content.units()){
				content.unlock();
			}
			for(UnlockableContent content : content.blocks()){
				content.unlock();
			}
			for(UnlockableContent content : content.sectors()){
				content.unlock();
			}
			for(UnlockableContent content : content.statusEffects()){
				content.unlock();
			}
		});
		
		cont.button("Settings", () -> {
			new NHSetting.SettingDialog().show();
		});
		
		cont.button("Interp", () -> {
			BaseDialog dialog = new BaseDialog("Interpolation", Styles.fullDialog);
			dialog.setFillParent(true);
			dialog.cont.pane(t -> {
				setFillParent(true);
				float unitLength = 2f;
				float offset = 70 * unitLength;
				float len = 100;
				float sigs = 100;
				Seq<Field> fields = new Seq<>();
				fields.addAll(Interp.class.getFields());
				fields.addAll(NHInterp.class.getFields());
				int i = 0;
				for(Field field : fields){
					if(Interp.class.isAssignableFrom(field.getType())){
						if(i++ % 5 == 0)t.row();
						try{
							Interp interp = (Interp)field.get(null);
							
							Table table = new Table(Tex.pane){{
								fill(Tex.clear, inner -> {
									Label l = inner.add(field.getName()).color(Color.white).align(Align.topLeft).get();
									l.getStyle().background = Styles.black3;
								});
							}
								@Override
								public void draw(){
									background(Tex.pane);
									super.draw();
									
									Lines.stroke(unitLength);
									Draw.color(Color.gray);
									Lines.line(x, y + offset, x + width, y + offset, false);
									Lines.line(x + offset, y, x + offset, y + height, false);
									Drawf.arrow(x, y + offset, x + width, y + offset, width - unitLength * 5.75f, unitLength * 5, Pal.gray);
									Drawf.arrow(x + offset, y, x + offset, y + height, height - unitLength * 5.75f, unitLength * 5, Pal.gray);
									
									Draw.color(Color.gray);
									Lines.line(x + offset, y + offset + unitLength * len, x + offset + unitLength * len, y + offset + unitLength * len, false);
									Lines.line(x + offset + unitLength * len, y + offset, x + offset + unitLength * len, y + offset + unitLength * len, false);
									
									Fill.square(x + offset + unitLength * len, y + offset + unitLength * len, unitLength * 3, 45);
									Draw.color(Pal.accent);
									
									Lines.beginLine();
									
									for(float i = 0; i < len; i += len / sigs){
										Lines.linePoint(x + i * unitLength + offset, y + interp.apply(i / sigs) * len * unitLength + offset);
									}
									
									Lines.endLine(false);
									Draw.reset();
									
									background(Tex.clear);
									super.draw();
								}
							};
							
							t.table(table1 -> {
								table1.add(table).size(len * unitLength + offset * 2).pad(OFFSET / 2).row();
//								table1.pane(in -> in.add(field.getName()).fill()).growX().height(LEN / 2 + OFFSET);
							});
						}catch(IllegalAccessException ignored){}
					}
				}
			}).grow();
			dialog.addCloseButton();
			dialog.show();
		});
		
		cont.row();
		
		cont.button("UnlockSingle", () -> {
			new BaseDialog("UNLOCK"){{
				Seq<UnlockableContent> all = new Seq<>().addAll(content.units().select(c -> !c.isHidden())).addAll(content.blocks().select(c -> !c.isHidden())).addAll(content.items().select(c -> !c.isHidden())).addAll(content.liquids()).addAll(content.statusEffects().select(c -> !c.isHidden())).addAll(content.sectors().select(c -> !c.isHidden())).addAll(content.planets().select(c -> !c.isHidden())).as();
				
				cont.pane(t -> {
					for(int i = 0; i < all.size; i++){
						if(i % 4 == 0)t.row();
						
						UnlockableContent c = all.get(i);
						t.table(Tex.pane, table -> {
							table.image(c.fullIcon).size(LEN / 2);
							table.add(c.localizedName).padLeft(OFFSET / 2);
							table.button(Icon.lock, Styles.clearPartiali, () -> {
								if(c.unlocked())c.clearUnlock();
								else c.unlock();
							}).size(LEN / 2).update(b -> {
								if(c.unlocked())b.getStyle().imageUp = Icon.lockOpen;
								else b.getStyle().imageUp = Icon.lock;
							});
						}).fill().pad(OFFSET / 4);
					}
				}).grow();
				
				addCloseButton();
			}}.show();
		});
		
		cont.button("Hack", () -> {
			ScreenInterferencer.generate(360);
		});
		
		cont.button("Weathers", () -> {
			BaseDialog dialog = new BaseDialog("");
			dialog.cont.pane(t -> {
				t.image().growX().height(OFFSET / 4).pad(OFFSET / 2).color(Pal.accent).row();
				t.pane(table -> {
					for(Content content : content.getBy(ContentType.weather)){
						Weather c = (Weather)content;
						table.button(c.localizedName, () -> {
							Groups.weather.add(c.create(5f));
						}).growX().fillY().row();
					}
				}).grow().row();
				t.image().growX().height(OFFSET / 4).pad(OFFSET / 2).color(Pal.accent).row();
				t.button("Remove", () -> Groups.weather.clear()).growX().height(LEN);
			}).grow();
			dialog.addCloseButton();
			dialog.show();
		});
		
		cont.button("Generate Icon", () -> {
			ui.loadAnd("[accent]Generating", () -> {
				content.units().each(u -> {
					if(u.name.contains(NewHorizon.MOD_NAME)){
						NHPixmap.saveUnitPixmap(Core.atlas.getPixmap(u.fullIcon).crop(), u);
					}
				});
				
				NHPixmap.saveAddProcessed();
			});
		}).disabled(b -> !NHPixmap.isDebugging());
		
		cont.row();
		
		cont.button("cores iterate", () -> {
			Seq<CoreBlock.CoreBuild> cores = state.teams.cores(state.rules.waveTeam);
			Seq<Action> actions = new Seq<>(cores.size * 2);
			
			for(int i = 0; i < cores.size; i++){
				CoreBlock.CoreBuild core = cores.get(i);
				actions.add(UIActions.moveTo(core.x, core.y, 2f, Interp.circleOut));
				actions.add(Actions.parallel(
					UIActions.labelAct(
					"Team<[#" + core.team.color +  "]" + core.team.name.toUpperCase() +
						"[]>: @@@" +
						core.block.localizedName + " [[" + core.tileX() + ", " + core.tileY() + "]", 0.5f, 1.5f, Interp.linear, t -> {
							if(!core.team.emoji.isEmpty()){
								t.add(core.team.emoji).padRight(OFFSET);
							}
						}
					)
				));
			}
			
			Seq<Action> acts = new Seq<>();
			
			acts.addAll(Actions.run(() -> control.pause()), Actions.delay(2f)).addAll(actions).add(Actions.run(() -> control.resume()));
			
			UIActions.actionSeq(acts.toArray(Action.class));
		});

		cont.button("Add Rand Progress Bar", () -> {
			float time = Mathf.random(240f, 600f);
			String name = String.valueOf(Time.millis());
			
			CutsceneScript.curUpdater.add(() -> {
				CutsceneScript.reload(name, Time.delta, time, () -> true, () -> true, () -> state.rules.tags.remove(name));
			});
			
			Time.runTask(30f, () -> {
				UIActions.reloadBar(
						name,
						time, () -> String.valueOf(time), () -> Pal.heal
				);
			});
			
		});
		
		cont.button("Bundle Tool", InternalTools::patchBundle);
		
		cont.button("Skip Wave", () -> {
			state.wave += 10;
		});
		
		cont.row();
		
		cont.button("Warp", () -> Groups.unit.each(u -> WarpUnit.warp(u, 45)));
		
		cont.button("Update Sort Map", () -> TileSortMap.registerTeam(Team.purple));
		
		cont.button("Analyses Sort Map", () -> TileSortMap.getTeamMap(Team.purple).analysis());
		
		cont.button("Show Sort Map", () -> TileSortMap.getTeamMap(Team.purple).showAsDialog(TileSortMap.ValueCalculator.healthSqrt));
		
		cont.row();
		
		cont.button("Hyper Warp", () -> Groups.unit.each(u -> HyperSpaceWarper.Carrier.create(u, new Vec2().set(player))));
		
		cont.button("Add Bars", UnitInfo::addBars);
		
		cont.button("Sounds", () -> {
			new BaseDialog("ICONS"){{
				addCloseButton();
				
				Class<?> c = Sounds.class;
				Seq<Field> fields = Seq.with(Sounds.class.getFields()).and(NHSounds.class.getFields());
				
				cont.pane(t -> {
					int index = 0;
					for(Field f : fields){
						try{
							if(Sound.class.isAssignableFrom(f.getType())){
								if(index % 6 == 0) t.row();
								t.table(inner -> {
									inner.table(Tex.pane, de -> {
										de.margin(6f);
										de.add(f.getName()).growX();
										de.button(Icon.play, Styles.clearPartiali, () -> {
											try{
												((Sound)f.get(null)).play();
											}catch(IllegalAccessException e){
												e.printStackTrace();
											}
										}).growY().scaling(Scaling.fit);
									}).size(LEN * 3, LEN).pad(OFFSET / 3);
								}).size(LEN * 3, LEN).pad(OFFSET / 3);
								index++;
							}
						}catch(IllegalArgumentException err){
							throw new IllegalArgumentException(err);
						}
					}
				}).grow();
			}}.show();
		});
		
		addCloseButton();
	}
	
	public TableTexDebugDialog(String title, Dialog.DialogStyle style) {
		super(title, style);
	}
}































